package com.metaquanta.jove.visualization

import com.jme3.scene.shape.Quad
import com.jme3.scene.Geometry
import com.jme3.texture.Texture2D
import com.metaquanta.jove.JME3Application
import com.metaquanta.jove.position.Position

/**
 * Created by matthew on 4/25/14.
 */
class ScreenVisualizer(pos:Position, app:JME3Application)
  extends VisualizerNode(app) with Visualizer {

  var screen:Geometry = null

  def update(tpf:Float) {
    if(!paused && imageStream != null && imageStream.ready) {
      val image = imageStream.next
      if(screen == null) {
        val (width, height) = (image.getWidth, image.getHeight)
        screen = new Geometry("Quad",
          new Quad(width.toFloat / height.toFloat, 1f)
        )
        screen.setMaterial(mat)
        setLocalTranslation(pos.position(width.toFloat / height.toFloat, 1f))
        setLocalRotation(pos.orientation(width.toFloat / height.toFloat, 1f))
        attachChild(screen)
      }
      val tex:Texture2D = new Texture2D(image)
      mat.setTexture("ColorMap", tex)
    }
  }
}
