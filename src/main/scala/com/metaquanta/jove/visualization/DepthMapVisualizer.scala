package com.metaquanta.jove.visualization

import com.jme3.scene.Node
import com.jme3.texture.Image
import org.jmonkeyengine.hub.ogli.RawPointCloudGraphGenerator
import com.metaquanta.jove.{JME3Application, PipeElement}
import com.jme3.renderer.queue.RenderQueue
import com.jme3.math.Vector3f
import com.metaquanta.jove.position.Position


/**
 * Created by matthew on 4/26/14.
 */
class DepthMapVisualizer(name:String, src:PipeElement, index:Int, app:JME3Application, pos:Position)
  extends VisualizationNode(name, src, index, app) {

  var n:Node = new Node
  attachChild(n)

  def update(tpf:Float) {
    val f = nextFrame
    if(f.isDefined) {
      t+=tpf
      if(!app.getGuiNode.hasChild(hudNode)) {
        app.pushGuiNodeChild(hudNode)
      }
      updateHudNode
      val (points, colors) = generatePoints(f.get)
      val generator = new RawPointCloudGraphGenerator(app.getAssetManager)
      detachChild(n)
      val n2 = new Node
      setLocalTranslation(pos.position(f.get.getWidth.toFloat/f.get.getHeight.toFloat, 1f))
      setLocalRotation(pos.orientation(f.get.getWidth.toFloat/f.get.getHeight.toFloat, 1f))

      n2.attachChild(generator.generatePointCloudGraph(points,colors))
      detachChild(n)
      attachChild(n2)
      n=n2

    }
  }

  def generatePoints(map:Image): (Array[Float], Array[Float]) = {
    val data = map.getData(0)
    val result: Array[Float] = new Array[Float](map.getWidth*map.getHeight*3)
    val colors: Array[Float] = new Array[Float](map.getWidth*map.getHeight*4)
    data.position(0)
    var i: Int = 0
    while (i < map.getWidth*map.getHeight) {
      {
        result(i * 3) = (i % map.getWidth).toFloat/map.getHeight
        result(i * 3 + 1) = (i / map.getWidth).toFloat/map.getHeight
        result(i * 3 + 2) = (data.get(i*3)+data.get(i*3+1)+data.get(i*3+2)).toFloat/(255*3)

        colors(i * 4) = (i % map.getWidth).toFloat/map.getWidth
        colors(i * 4 + 1) = (i / map.getWidth).toFloat/map.getWidth
        colors(i * 4 + 2) = result(i * 3 + 2)
        // Cull out some noise and the flat values
        colors(i * 4 + 3) = if (result(i * 3 + 2) <  .0001f || result(i * 3 + 2) >  .99f) 0f else 1f
      }
      ({
        i += 1; i - 1
      })
    }

    (result, colors)
  }
}
