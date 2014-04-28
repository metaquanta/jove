package com.metaquanta.jove.vizualization

import com.jme3.scene.Node
import com.jme3.texture.Image
import org.jmonkeyengine.hub.ogli.RawPointCloudGraphGenerator
import com.metaquanta.jove.{VisualizationNode, JME3Application, PipeElement}


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
      n = new Node
      n.attachChild(generator.generatePointCloudGraph(points,colors))
      attachChild(n)
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
        result(i * 3) = (i % map.getWidth).toFloat/map.getWidth
        result(i * 3 + 1) = (i / map.getWidth).toFloat/map.getWidth
        result(i * 3 + 2) = (data.get(i*3)+data.get(i*3+1)+data.get(i*3+2)).toFloat/(255*3)

        colors(i * 4) = (i % map.getWidth).toFloat/map.getWidth
        colors(i * 4 + 1) = (i / map.getWidth).toFloat/map.getWidth
        colors(i * 4 + 2) = result(i * 3 + 2)
        colors(i * 4 + 3) = 1f//if (result(i * 3 + 2) <  .0001f) 0f else 1f
      }
      ({
        i += 1; i - 1
      })
    }

    return (result, colors)
  }
}
