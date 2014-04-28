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
class VisualizationNode (input:PipeElement, index:Int) extends Node {
  var t:Float = 0
  var lastFrameAt:Float = 0

  lazy val (width, height) = {
    Await.ready(frameFuture, Duration.Inf)
    (frameFuture.value.get.get.head.getWidth,
      frameFuture.value.get.get.head.getHeight)
  }

  def dimensions:(Int,Int) = {
    (width, height)
  }

  var frameFuture:Future[List[Image]] = input.getImage()

  def nextFrame:Option[Image] = {
    var f:Option[Image] = None
    if(frameFuture.isCompleted) {
      lastFrameAt = t
      f = Option(frameFuture.value.get.get(index))
      frameFuture = input.getImage
    }
    f
  }

}
