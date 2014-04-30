package com.metaquanta.jove

import com.jme3.app.SimpleApplication
import com.jme3.math.Vector3f
import com.jme3.scene.{CameraNode, Node}
import com.metaquanta.jove.visualization.{VisualizationNode, DepthMapVisualizer, ScreenVisualizer}

/**
 * Created by matthew on 4/25/14.
 */
class JME3Application extends SimpleApplication {
  start()

  var visualizers = List[VisualizationNode]()

  def simpleInitApp() {
    getCamera.setLocation(new Vector3f(0, 0, 0))
    getCamera.lookAt(new Vector3f(1, 0, 0), Vector3f.UNIT_Y)
    synchronized {
      println("notifying...")
      notify()
    }
  }

  override def simpleUpdate(tpf:Float) {
    visualizers.foreach(s => {
      if(!rootNode.hasChild(s)) {
        rootNode.attachChild(s)
      }
    })
    visualizers.foreach(s => s.update(tpf))
  }

  def attachVisualizer(v:VisualizationNode) {
    // This gets called outside jme's thread. We have to attach on update.
    //rootNode.attachChild(v)
    visualizers = visualizers:+v
  }

  def getFont = {guiFont}

  var guiNodes = 0
  def pushGuiNodeChild(n:Node) {
    n.setLocalTranslation(0, settings.getHeight() - 20*guiNodes, 0)
    guiNodes += 1
    guiNode.attachChild(n)
  }
}
