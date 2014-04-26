import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.scene.shape.Quad
import com.jme3.scene.{Geometry, Node}
import com.jme3.texture.Texture2D
//import com.jme3.ui.Picture
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
    println("Screen Dimensions:"+width+","+height)
    val b = new Geometry("Quad", new Quad(width.toFloat/height.toFloat, 1f))
    //val b = new Geometry("Quad", new Quad(1f, 1f))
//    val b = new Picture("s")
//    //b.setWidth(width/height)
//    b.setWidth(100)
//    //b.setHeight(1f)
//    b.setHeight(100)
//    b.updateGeometricState()
//    b.setPosition(0,0)
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
