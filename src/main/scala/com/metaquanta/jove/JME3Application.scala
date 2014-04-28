package com.metaquanta.jove

import com.jme3.app.SimpleApplication
import com.jme3.math.Vector3f
import com.jme3.scene.Node

/**
 * Created by matthew on 4/25/14.
 */
class JME3Application extends SimpleApplication {
  start()

  var screens = List[ScreenVisualizer]()
  var dm:DepthMapVisualizer = null

  def simpleInitApp() {
    synchronized {
      println("notifying...")
      notify()
    }
  }

  override def simpleUpdate(tpf:Float) {
    screens.foreach(s => {
      if(!rootNode.hasChild(s)) {
        rootNode.attachChild(s)
      }
    })
    screens.foreach(s => s.update(tpf))

    if(dm != null) {
      if(!rootNode.hasChild(dm)) {
        rootNode.attachChild(dm)
      }
      dm.update(tpf)
    }
  }

  def attachScreen(s:ScreenVisualizer, p:Vector3f) {
    s.setLocalTranslation(p)
    //rootNode.attachChild(s)
    screens = screens:+s
  }

  def getFont = {guiFont}

  def attachDepthMap(d:DepthMapVisualizer, p:Vector3f) {
    dm = d
  }

  var guiNodes = 0
  def pushGuiNodeChild(n:Node) {
    n.setLocalTranslation(0, settings.getHeight() - 20*guiNodes, 0)
    guiNodes += 1
    guiNode.attachChild(n)
  }
}
