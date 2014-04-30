package com.metaquanta.jove.visualization

import com.jme3.material.Material
import com.jme3.scene.shape.Quad
import com.jme3.scene.Geometry
import com.jme3.texture.Texture2D
import com.metaquanta.jove.{JME3Application, PipeElement}
import com.metaquanta.jove.position.Position
import com.jme3.renderer.queue.RenderQueue
import com.jme3.math.Vector3f

/**
 * Created by matthew on 4/25/14.
 */
class ScreenVisualizer(name:String, src:PipeElement, index:Int, app:JME3Application, pos:Position)
  extends VisualizationNode(name, src, index, app) {

  val mat = new Material(app.getAssetManager(),
    "Common/MatDefs/Misc/Unshaded.j3md")
  lazy val box = {
    val (width, height) = dimensions
    val b = new Geometry("Quad", new Quad(width.toFloat/height.toFloat, 1f))
    b.setMaterial(mat)

    attachChild(b)
    setLocalTranslation(pos.position(width.toFloat/height.toFloat, 1f))
    setLocalRotation(pos.orientation(width.toFloat/height.toFloat, 1f))
    b
  }

  def update(tpf:Float) {
    t+=tpf
    if(!app.getGuiNode.hasChild(hudNode)) {
      app.pushGuiNodeChild(hudNode)
    }
    updateHudNode
    val f = nextFrame
    if(f.isDefined) {
      val ftex:Texture2D = new Texture2D(f.get)
      mat.setTexture("ColorMap", ftex)
      box
    }
  }
}
