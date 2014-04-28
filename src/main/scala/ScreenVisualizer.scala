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
class ScreenVisualizer(src:PipeElement, index:Int, app:JME3Application) extends VisualizationNode(src, index) {
  val mat = new Material(app.getAssetManager(),
    "Common/MatDefs/Misc/Unshaded.j3md")
  lazy val box = {
    val (width, height) = dimensions
    val b = new Geometry("Quad", new Quad(width.toFloat/height.toFloat, 1f))
    b.setMaterial(mat)
    attachChild(b)
    b
  }

  def update(tpf:Float) {
    t+=tpf
    val f = nextFrame
    if(f.isDefined) {
      val ftex:Texture2D = new Texture2D(f.get)
      mat.setTexture("ColorMap", ftex)
      box
    }
  }
}
