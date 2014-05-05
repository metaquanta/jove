package com.metaquanta.jove

import com.jme3.app.SimpleApplication
import com.jme3.math.{Ray, Vector3f}
import com.jme3.scene.Node
import com.metaquanta.jove.visualization.VisualizerNode
import com.jme3.input.controls.{KeyTrigger, ActionListener}
import com.jme3.input.KeyInput
import com.jme3.collision.CollisionResults

/**
 * Created by matthew on 4/25/14.
 */
class JME3Application extends SimpleApplication {
  // This is an example jME3 app for use with jove.
  // todo: move the necessary interface to a trait.
  start()

  var visualizers = List[VisualizerNode]()
  var guiNodes = List[Node]()

  def simpleInitApp() {
    initCam()
    // Set up the picker for pausing the visualizers
    val actionListener = new ActionListener() {
      def onAction(name:String, keyPressed:Boolean, tpf:Float) {
        if(name.equals("ResetCam") && !keyPressed) {
          // Bring us back to <0,0,0> so we are looking at all the screens
          initCam()
        }
        if(name.equals("Pause") && !keyPressed) {
          // From: http://hub.jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_picking
          // 1. Reset results list.
          val results = new CollisionResults()
          // 2. Aim the ray from cam loc to cam direction.
          val ray = new Ray(cam.getLocation, cam.getDirection)
          // 3. Collect intersections between Ray and Shootables in results
          // list.
          rootNode.collideWith(ray, results)
          for(i <- 0 until results.size()) {
            val target = results.getCollision(i).getGeometry.getParent
            target match {
              // If we picked a visualizer, pause it
              case target:VisualizerNode => target.pause
            }
          }
        }
      }
    }
    inputManager.addMapping("ResetCam", new KeyTrigger(KeyInput.KEY_R))
    inputManager.addListener(actionListener, "ResetCam")
    inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P))
    inputManager.addListener(actionListener, "Pause")

    synchronized {
      println("...done")
      notify()
    }
  }

  def initCam() {
    getCamera.setLocation(new Vector3f(0, 0, 0))
    getCamera.lookAt(new Vector3f(1, 0, 0), Vector3f.UNIT_Y)
  }

  override def simpleUpdate(tpf:Float) {
    // The scene graph can only be updated in this thread.
    // All attachChild()s belong here
    visualizers.foreach(s => {
      if(!rootNode.hasChild(s)) {
        rootNode.attachChild(s)
      }
    })
    guiNodes.foreach(n => {
      if(!guiNode.hasChild(n)) {
        // todo: This should be abstracted
        n.setLocalTranslation(0, settings.getHeight - 20 * guiNodesIndex, 0)
        guiNodesIndex += 1
        guiNode.attachChild(n)
      }
    })
    visualizers.foreach(s => s.update(tpf))
  }

  def attachVisualizer(v:VisualizerNode) {
    visualizers = visualizers :+ v
  }

  def getFont = { guiFont }

  // todo: We should not maintain an index
  var guiNodesIndex = 0

  def attachGuiNodeChild(n:Node) {
    guiNodes = guiNodes :+ n
  }
}
