package com.metaquanta.jove

import com.metaquanta.jove.stage.Stage
import scala.concurrent._
import org.opencv.core.{Core, Mat}
import com.jme3.texture.Image
import java.nio.ByteBuffer
import com.metaquanta.jove.visualization.{ImageStream, Visualizer}
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import com.jme3.font.BitmapText
import com.jme3.math.ColorRGBA
import com.jme3.scene.Node
import scala.compat.Platform

/**
 * Created by matthew on 4/30/14.
 */
class Jove(app:JME3Application) {

  def addStage(stage:Stage, inputs:List[ElementOutputSplit], stats:String):Stream[ElementOutputSplit] = {
    // Create an iterator that returns an OutputSplit with the requested index
    val elem = new Element(stage, inputs, stats)
    Stream.from(0).map(i => new ElementOutputSplit(elem, i))
  }

  def addStage(stage:Stage, input:ElementOutputSplit, stats:String):Stream[ElementOutputSplit] = {
    addStage(stage, List(input), stats)
  }

  def addStage(stage:Stage):Stream[ElementOutputSplit] = {
    addStage(stage, List(), null)
  }

  def addVisualizer(visualizer:Visualizer, input:ElementOutputSplit) {
    new VisualizerElement(visualizer, input)
  }

  class VisualizerElement(visualizer:Visualizer, input:ElementOutputSplit) {
    var frameFuture:Future[Image] = input.getImage()

    visualizer.setImageStream(new Object with ImageStream {
      def next:Image = {
        Await.ready(frameFuture, Duration.Inf)
        val img:Image = frameFuture.value.get.get
        // start waiting on the next frame
        frameFuture = input.getImage()
        img
      }

      def ready:Boolean = {
        frameFuture.isCompleted
      }
    })
  }

  class ElementOutputSplit(val element:Element, val index:Int) {

    // All these do is pull the desired element out of the list
    def getImage():Future[Image] = {
      for {
        imgs <- element.getImage()
        img <- future(imgs(index))
      } yield img
    }

    def getFrame():Future[Mat] = {
      for {
        fs <- element.getFrame()
        f <- future(fs(index))
      } yield f
    }
  }

  class Element(cap:Stage, in:List[ElementOutputSplit], stats:String) {
    var nextFrame:Future[List[Mat]] = null

    var lastFrame:Long = 0
    var fps:Float = 0

    def getFrame():Future[List[Mat]] = {
      // We always return a future that will return the /next/ frame
      if(nextFrame == null || nextFrame.isCompleted) {
        // Check the hud stats
        if(stats != null && !app.getGuiNode.hasChild(hudNode)) {
          app.attachGuiNodeChild(hudNode)
        }
        val thisFrame = Platform.currentTime
        fps = 1000/(thisFrame-lastFrame)
        lastFrame = thisFrame
        if(stats != null) updateHudNode
        val inmats = in.map(x => x.element.getFrame)
        nextFrame = for {
          mats <- Future.sequence(inmats)
          f <- future(cap.getFrame(mats.flatten))
        } yield f
      }
      return nextFrame
    }

    def getImage():Future[List[Image]] = {
      for {
        mats <- getFrame
        image <- future(mats.map(x => mat2Image(x)))
      } yield (image)
    }

    lazy val hudText = new BitmapText(app.getFont, false)

    def hudNode:Node = {
      hudText.setSize(app.getFont.getCharSet.getRenderedSize)
      hudText.setColor(ColorRGBA.Green)
      updateHudNode
      hudText
    }

    def updateHudNode {
      hudText.setText(stats + " fps:" + fps)
    }



    def mat2Image(mat:Mat):Image = {
      // OpenCV mats are in reverse order of what jME3 expects
      val flipdst = new Mat
      Core.flip(mat, flipdst, 0)

      // Perform the voodoo magic
      val len: Int = flipdst.width * flipdst.height * flipdst.channels()
      val byteBuff: ByteBuffer = ByteBuffer.allocateDirect(len)
      val bytes: Array[Byte] = new Array[Byte](len)
      flipdst.get(0, 0, bytes)
      new Image(Image.Format.BGR8,
        flipdst.width, flipdst.height,
        byteBuff.put(bytes))
    }

  }

}
