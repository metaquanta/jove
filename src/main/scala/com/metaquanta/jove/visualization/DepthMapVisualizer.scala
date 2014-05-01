package com.metaquanta.jove.visualization

import com.jme3.scene.{Geometry, Node}
import com.jme3.texture.Image
import org.jmonkeyengine.hub.ogli.RawPointCloudGraphGenerator
import com.metaquanta.jove.{JME3Application}
import com.metaquanta.jove.position.Position
import com.jme3.scene.shape.Quad
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.material.RenderState.BlendMode
import com.jme3.renderer.queue.RenderQueue.Bucket

/**
 * Created by matthew on 4/26/14.
 */
class DepthMapVisualizer(pos:Position, app:JME3Application)
  extends VisualizerNode(app) with Visualizer {

  var box = new Node()
  attachChild(box)
  var invisibleHitScreen:Geometry = null

  val invisible = new Material(app.getAssetManager(),
    "Common/MatDefs/Misc/Unshaded.j3md")
  // BlackNoAlpha is really "Invisible" and Black is really "BlackOpaque"
  invisible.setColor("Color", ColorRGBA.BlackNoAlpha)
  invisible.getAdditionalRenderState().setBlendMode(BlendMode.Alpha)

  def update(tpf:Float) {
    if (!paused && imageStream != null && imageStream.ready) {
      val image = imageStream.next
      if (invisibleHitScreen == null){
        println("Creating HitScreen!")
        val (width, height) = (image.getWidth, image.getHeight)
        invisibleHitScreen = new Geometry("Quad", new Quad(width.toFloat/height.toFloat, 1f))
        invisibleHitScreen.setMaterial(invisible)
        invisibleHitScreen.setQueueBucket(Bucket.Transparent)

        attachChild(invisibleHitScreen)
        //app.getRootNode.attachChild(this)
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
