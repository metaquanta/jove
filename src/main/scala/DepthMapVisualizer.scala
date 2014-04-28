import com.jme3.material.Material
import com.jme3.scene.shape.Quad
import com.jme3.scene.{Mesh, Geometry, Node}
import com.jme3.terrain.geomipmap.TerrainQuad
import com.jme3.terrain.heightmap.{AbstractHeightMap, ImageBasedHeightMap}
import com.jme3.texture.Texture2D
import java.util.Random
import com.jme3.texture.Image
import scala.concurrent.Await
import org.jmonkeyengine.hub.ogli.{RawPointCloudGraphGenerator}


/**
 * Created by matthew on 4/26/14.
 */
class DepthMapVisualizer(src:PipeElement, index:Int, app:JME3Application) extends VisualizationNode(src, index) {
  var n:Node = new Node
  attachChild(n)

  def update(tpf:Float) {
    val f = nextFrame
    if(f.isDefined) {
      val (points, colors) = generatePoints(f.get)
      val generator = new RawPointCloudGraphGenerator(app.getAssetManager)
      detachChild(n)
      n = new Node
      n.attachChild(generator.generatePointCloudGraph(points,colors))
      attachChild(n)
    }
  }

  def generatePoints(map:Image): (Array[Float], Array[Float]) = {
    val data = map.getData(0)
    val result: Array[Float] = new Array[Float](map.getWidth*map.getHeight*3)
    val colors: Array[Float] = new Array[Float](map.getWidth*map.getHeight*4)
    println("buffer is 3*" + map.getWidth +"*"+map.getHeight)
    data.position(0)
    println("Map: " + data.get())

    //val random: Random = new Random()

      var i: Int = 0
      while (i < map.getWidth*map.getHeight) {
        {
          result(i * 3) = (i % map.getWidth).toFloat/map.getWidth
          result(i * 3 + 1) = (i / map.getWidth).toFloat/map.getWidth
          result(i * 3 + 2) = (data.get(i*3)+data.get(i*3+1)+data.get(i*3+2)).toFloat/(255*3)

          colors(i * 4) = (i % map.getWidth).toFloat/map.getWidth
          colors(i * 4 + 1) = (i / map.getWidth).toFloat/map.getWidth
          colors(i * 4 + 2) = result(i * 3 + 2)
          colors(i * 4 + 3) = 1f//if (result(i * 3 + 2) <  .0001f) 0f else 1f
        }
        ({
          i += 1; i - 1
        })
      }

    return (result, colors)
  }
}
