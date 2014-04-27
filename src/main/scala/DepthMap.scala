import com.jme3.material.Material
import com.jme3.scene.shape.Quad
import com.jme3.scene.{Geometry, Node}
import com.jme3.terrain.geomipmap.TerrainQuad
import com.jme3.terrain.heightmap.{AbstractHeightMap, ImageBasedHeightMap}
import com.jme3.texture.Texture2D
import scala.concurrent.Await

/**
 * Created by matthew on 4/26/14.
 */
class DepthMap(src:PipeElement, index:Int, app:JME3Application) extends Node {
  val mat = new Material(app.getAssetManager(),
    "Common/MatDefs/Misc/Unshaded.j3md")

  var frameFuture = src.getImage()

  var terrain:TerrainQuad = null

  def update(tpf:Float) {
    if(frameFuture.isCompleted) {
      if(terrain != null) {
        detachChild(terrain)
      }
      val f = frameFuture.value.get.get(index)
      val ftex:Texture2D = new Texture2D(f)
      val img = ftex.getImage()
      img.setWidth(img.getHeight)
      val heightmap = new ImageBasedHeightMap(img)
      heightmap.load()
      terrain = new TerrainQuad(
        "my terrain",               // name
        1,                         // tile size
        f.getHeight,                        // block size
        heightmap.getHeightMap())
      terrain.setMaterial(mat)
      terrain.setLocalTranslation(0, 0, 0)
      //terrain.setLocalScale(2f, 1f, 2f);
      attachChild(terrain)
      frameFuture = src.getImage
    }
  }
}
