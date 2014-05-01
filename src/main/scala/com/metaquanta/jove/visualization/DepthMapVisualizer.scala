package com.metaquanta.jove.visualization

import com.jme3.scene.Node
import com.jme3.texture.Image
import org.jmonkeyengine.hub.ogli.RawPointCloudGraphGenerator
import com.metaquanta.jove.{JME3Application}
import com.metaquanta.jove.position.Position


/**
 * Created by matthew on 4/26/14.
 */
class DepthMapVisualizer(pos:Position, app:JME3Application)
  extends VisualizerNode(app) with Visualizer {

  var box = new Node()
  attachChild(box)

  def update(tpf:Float) {
    if (imageStream.ready) {
      val image = imageStream.next
      if (!app.getRootNode.hasChild(this)){
        app.getRootNode.attachChild(this)
      }
      val (points, colors) = generatePoints(image)
      val generator = new RawPointCloudGraphGenerator(app.getAssetManager)
      detachChild(box)
      val n = new Node
      setLocalTranslation(pos.position(image.getWidth.toFloat / image.getHeight.toFloat, 1f))
      setLocalRotation(pos.orientation(image.getWidth.toFloat / image.getHeight.toFloat, 1f))

      n.attachChild(generator.generatePointCloudGraph(points, colors))
      detachChild(box)
      attachChild(n)
      box = n
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
