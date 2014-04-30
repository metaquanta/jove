package com.metaquanta.jove.visualization

import com.jme3.texture.Image
import com.jme3.material.Material
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.metaquanta.jove.JME3Application

/**
 * Created by matthew on 4/30/14.
 */
abstract class VisualizationNode(app:JME3Application) extends Node {
  var image:Image = null
  var newImage:Boolean = false

  app.attachVisualizer(this)

  def setImage(img:Image):Boolean = {
    image=img
    newImage=true

    true
  }

  val mat = new Material(app.getAssetManager(),
    "Common/MatDefs/Misc/Unshaded.j3md")

  def update(tpf:Float)

}
