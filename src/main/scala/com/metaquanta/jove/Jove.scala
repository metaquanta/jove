package com.metaquanta.jove

import com.metaquanta.jove.stage.Stage
import scala.concurrent._
import org.opencv.core.{Core, Mat}
import com.jme3.texture.Image
import java.nio.ByteBuffer
import com.metaquanta.jove.visualization.{Visualizer}
import ExecutionContext.Implicits.global
import com.metaquanta.jove.position.Position
import scala.concurrent.duration.Duration
import com.jme3.font.BitmapText
import com.jme3.scene.Node
import com.jme3.math.ColorRGBA
import scala.util.Try

/**
 * Created by matthew on 4/30/14.
 */
class Jove(app:JME3Application) {

//  var visualizers = List[VisualizerElement]()

  def addStage(name:String, stage:Stage, inputs:List[ElementOutputSplit]):Stream[ElementOutputSplit] = {
    val element = new Element(stage, inputs)
    Stream.from(0).map(i => new ElementOutputSplit(element, i))
  }

  def addStage(name:String, stage:Stage, input:ElementOutputSplit):Stream[ElementOutputSplit] = {
    addStage(name, stage, List(input))
  }

  def addStage(name:String, stage:Stage):Stream[ElementOutputSplit] = {
    addStage(name, stage, List())
  }

  def addVisualizer(visualizer:Visualizer, input:ElementOutputSplit) {
    //visualizers:+
    new VisualizerElement(visualizer, input)
  }

  class VisualizerElement(visualizer:Visualizer, input:ElementOutputSplit) {
    var frameFuture:Future[Image] = input.getImage()
    frameFuture onComplete onFrameComplete

    def onFrameComplete(img:Try[Image]) {
        if(img.isSuccess) {
          img.get.synchronized {
            visualizer.setImage(img.get)
            img.get.wait()
          }
        }
        frameFuture = input.getImage()
        frameFuture onComplete onFrameComplete
    }
  }

  class ElementOutputSplit(val element:Element, val index:Int) {
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

  class Element(cap:Stage, in:List[ElementOutputSplit]) {
    var nextFrame:Future[List[Mat]] = null

    def getFrame():Future[List[Mat]] = {
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
      // Remap the mat
      // This is really slow, I don't know why
      //Imgproc.remap( mat, flipdst, flipmapx, flipmapy, 1) //Imgproc.CV_INTER_LINEAR = 1 and is private for some reason
      val flipdst = new Mat
      Core.flip(mat, flipdst, 0)

      // Perform the voodoo magic
      val width: Int = flipdst.width
      val height: Int = flipdst.height
      val channels: Int = flipdst.channels()
      val len: Int = width * height * channels
      val byteBuff: ByteBuffer = ByteBuffer.allocateDirect(len)
      val bytes: Array[Byte] = new Array[Byte](len)
      flipdst.get(0, 0, bytes)
      new Image(Image.Format.BGR8, width, height, byteBuff.put(bytes))
    }

  }

}
