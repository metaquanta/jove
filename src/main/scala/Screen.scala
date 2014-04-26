import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.scene.shape.Box
import com.jme3.scene.{Geometry, Node}
import com.jme3.texture.Texture2D
import scala.concurrent._
import scala.concurrent.duration._

/**
 * Created by matthew on 4/25/14.
 */
class Screen(src:PipeElement, index:Int, app:JME3Application) extends Node {
  var t:Float = 0
  var lastFrameAt:Float = 0
  val mat = new Material(app.getAssetManager(),
    "Common/MatDefs/Misc/Unshaded.j3md")
  lazy val box = {
    Await.ready(frameFuture, 0 nanos)
    val width = frameFuture.value.get.get.head.getWidth
    val height = frameFuture.value.get.get.head.getHeight
    val b = new Geometry("Box", new Box(width/height, 1f, 1f))
    b.setMaterial(mat)
    attachChild(b)
    b
  }

  var frameFuture = src.getImage()

  def update(tpf:Float) {
    t+=tpf
    if(frameFuture.isCompleted) {
      //println("frame time " + (t -lastFrameAt))
      lastFrameAt = t
      val f = frameFuture.value.get.get(index)
      val ftex:Texture2D = new Texture2D(frameFuture.value.get.get.head)
      mat.setTexture("ColorMap", ftex)
      box
      frameFuture = src.getImage
    }
  }
}
