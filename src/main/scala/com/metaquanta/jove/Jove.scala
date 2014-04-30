package com.metaquanta.jove

import com.metaquanta.jove.stage.Stage
import scala.concurrent._
import org.opencv.core.{Core, Mat}
import com.jme3.texture.Image
import java.nio.ByteBuffer
import com.metaquanta.jove.visualization.{ImageStream, Visualizer}
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
 * Created by matthew on 4/30/14.
 */
class Jove(app:JME3Application) {

  def addStage(name:String, stage:Stage, inputs:List[ElementOutputSplit]):Stream[ElementOutputSplit] = {
    // Create an iterator that returns an OutputSplit with the requested index
    Stream.from(0).map(i => new ElementOutputSplit(new Element(name, stage, inputs), i))
  }

  def addStage(name:String, stage:Stage, input:ElementOutputSplit):Stream[ElementOutputSplit] = {
    addStage(name, stage, List(input))
  }

  def addStage(name:String, stage:Stage):Stream[ElementOutputSplit] = {
    addStage(name, stage, List())
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

  class Element(name:String, cap:Stage, in:List[ElementOutputSplit]) {
    var nextFrame:Future[List[Mat]] = null

    def getFrame():Future[List[Mat]] = {
      // We always return a future that will return the /next/ frame
      if(nextFrame == null || nextFrame.isCompleted) {
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
