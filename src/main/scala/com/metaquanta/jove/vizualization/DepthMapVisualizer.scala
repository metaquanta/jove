package com.metaquanta.jove.vizualization

import com.jme3.scene.Node
import com.jme3.texture.Image
import org.jmonkeyengine.hub.ogli.RawPointCloudGraphGenerator
import com.metaquanta.jove.{JME3Application, PipeElement}
import com.jme3.renderer.queue.RenderQueue


/**
 * Created by matthew on 4/26/14.
 */
class DepthMapVisualizer(name:String, src:PipeElement, index:Int, app:JME3Application)
  extends VisualizationNode(name, src, index, app) {

  var n:Node = new Node
  attachChild(n)

  def update(tpf:Float) {
    val f = nextFrame
    if(f.isDefined) {
      val (points, colors) = generatePoints(f.get)
      val generator = new RawPointCloudGraphGenerator(app.getAssetManager)
      detachChild(n)
      val n2 = new Node
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

    return (result, colors)
  }
}
