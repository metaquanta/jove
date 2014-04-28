import com.jme3.font.BitmapText
import com.jme3.math.ColorRGBA
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Quad
import scala.concurrent.Await
import com.jme3.scene.{Geometry, Node}
import scala.concurrent._
import scala.concurrent.duration._
import com.jme3.texture.{Texture2D, Image}

/**
 * Created by matthew on 4/28/14.
 */
class VisualizationNode (name:String, input:PipeElement, index:Int, app:JME3Application) extends Node {
  var t:Float = 0
  var lastFrameAt:Float = 0
  var frameAt:Float = 0

  lazy val (width, height) = {
    val ff = frameFuture
    Await.ready(ff, Duration.Inf)
    (ff.value.get.get.head.getWidth,
      ff.value.get.get.head.getHeight)
  }

  def dimensions:(Int,Int) = {
    (width, height)
  }

  var frameFuture:Future[List[Image]] = input.getImage()

  def fps = {
    1f/(frameAt - lastFrameAt)
  }

  lazy val hudText = new BitmapText(app.getFont, false)

  def hudNode:Node = {
    hudText.setSize(app.getFont.getCharSet.getRenderedSize)
    hudText.setColor(ColorRGBA.Green)
    updateHudNode
    hudText
  }

  def updateHudNode {
    hudText.setText(name + " fps:" + fps)
  }

  def nextFrame:Option[Image] = {
    var f:Option[Image] = None
    if(frameFuture.isCompleted) {
      lastFrameAt = frameAt
      frameAt = t
      f = Option(frameFuture.value.get.get(index))
      frameFuture = input.getImage
    }
    f
  }

}
