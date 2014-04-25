import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.scene.shape.Box
import com.jme3.scene.{Geometry, Node}
import com.jme3.texture.Texture2D
import scala.concurrent._

/**
 * Created by matthew on 4/25/14.
 */
class Screen(src:Input, app:JME3Application) extends Node {
  var t:Float = 0
  var lastFrameAt:Float = 0
  val mat = new Material(app.getAssetManager(),
    "Common/MatDefs/Misc/Unshaded.j3md")
  val box = new Geometry("Box", new Box(src.width.toFloat/src.height.toFloat, 1f, 1f))
  box.setMaterial(mat)
  attachChild(box)

  var frameFuture = src.getImage()

  def update(tpf:Float) {
    t+=tpf
    if(frameFuture.isCompleted) {
      println("frame time " + (t -lastFrameAt))
      lastFrameAt = t
      val ftex:Texture2D = new Texture2D(frameFuture.value.get.get)
      mat.setTexture("ColorMap", ftex)
      frameFuture = src.getImage
    }
  }
}
