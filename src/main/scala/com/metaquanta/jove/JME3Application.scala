package com.metaquanta.jove

import com.jme3.app.SimpleApplication
import com.jme3.math.{Ray, Vector3f}
import com.jme3.scene.{CameraNode, Node}
import com.metaquanta.jove.visualization.{VisualizerNode, DepthMapVisualizer, ScreenVisualizer}
import com.jme3.input.controls.{KeyTrigger, ActionListener}
import com.jme3.input.KeyInput
import com.jme3.collision.CollisionResults

/**
 * Created by matthew on 4/25/14.
 */
class JME3Application extends SimpleApplication {
  start()

  var visualizers = List[VisualizerNode]()
  var guiNodes = List[Node]()

  def simpleInitApp() {
    initCam
    val actionListener = new ActionListener() {
      def onAction(name:String, keyPressed:Boolean, tpf:Float) {
        println("onAction:" + name)
        if (name.equals("ResetCam") && !keyPressed) {
          initCam
        }
        if (name.equals("Pause") && !keyPressed) {
          // From: http://hub.jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_picking
          // 1. Reset results list.
          val results = new CollisionResults()
          // 2. Aim the ray from cam loc to cam direction.
          val ray = new Ray(cam.getLocation(), cam.getDirection())
          // 3. Collect intersections between Ray and Shootables in results list.
          rootNode.collideWith(ray, results)
          // 4. Print results.
          System.out.println("----- Collisions? " + results.size() + "-----")
          for(i <- 0 until results.size()) {
            // For each hit, we know distance, impact point, name of geometry.
//            val dist = results.getCollision(i).getDistance();
//            val pt = results.getCollision(i).getContactPoint();
//            val hit = results.getCollision(i).getGeometry().getName();
            val target = results.getCollision(i).getGeometry.getParent
            target match {
              case target:VisualizerNode => target.pause
            }
            System.out.println("* Collision #" + i);
            //System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
          }
        }
      }
    }
    inputManager.addMapping("ResetCam",  new KeyTrigger(KeyInput.KEY_R))
    inputManager.addListener(actionListener,"ResetCam")
    inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P))
    inputManager.addListener(actionListener,"Pause")
    synchronized {
      println("notifying...")
      notify()
    }
  }

  def initCam {
    getCamera.setLocation(new Vector3f(0, 0, 0))
    getCamera.lookAt(new Vector3f(1, 0, 0), Vector3f.UNIT_Y)
  }

  override def simpleUpdate(tpf:Float) {
    visualizers.foreach(s => {
      if(!rootNode.hasChild(s)) {
        rootNode.attachChild(s)
      }
    })
    guiNodes.foreach(n => {
      if(!guiNode.hasChild(n)) {
        n.setLocalTranslation(0, settings.getHeight() - 20*guiNodesIndex, 0)
        guiNodesIndex += 1
        guiNode.attachChild(n)
      }
    })
    visualizers.foreach(s => s.update(tpf))
  }

  def attachVisualizer(v:VisualizerNode) {
    // This gets called outside jme's thread. We have to attach on update.
    //rootNode.attachChild(v)
    visualizers = visualizers:+v
  }

  def getFont = {guiFont}

  var guiNodesIndex = 0
  def attachGuiNodeChild(n:Node) {
    guiNodes = guiNodes:+n
  }
}
