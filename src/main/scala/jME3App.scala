/**
 * Created by matthew on 4/6/14.
 */

import com.jme3.app.SimpleApplication
import com.jme3.light.AmbientLight
import com.jme3.material.Material
import com.jme3.math._
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import com.jme3.texture.image.ImageRaster
import com.jme3.texture.{Texture2D, Image, Texture}
import scala.concurrent.Future

class jME3App extends SimpleApplication {
  var time = 0.0
  var boxMat1:Material = null
  var boxMat2:Material = null
  var boxMat3:Material = null
  var img1:Future[Image] = null;
  var img2:Future[Image] = null;
  var img3:Future[Image] = null;
  var getImg1:()=>Future[Image] = null;
  var getImg2:()=>Future[Image] = null;
  var getImg3:()=>Future[Image] = null;
  val box1 = new Geometry("Box", new Box(1, 1, 1))
//  box1.setLocalRotation(new Quaternion().fromAngleAxis(
//    FastMath.PI, Vector3f.UNIT_X))
  box1.setLocalTranslation(new Vector3f(-2,0,0))
  val box2 = new Geometry("Box", new Box(1, 1, 1))
//  box2.setLocalRotation(new Quaternion().fromAngleAxis(
//    FastMath.PI, Vector3f.UNIT_X))
  box2.setLocalTranslation(new Vector3f(2,0,0))
  val box3 = new Geometry("Box", new Box(1, 1, 1))
//  box3.setLocalRotation(new Quaternion().fromAngleAxis(
//    FastMath.PI, Vector3f.UNIT_X))
  box3.setLocalTranslation(new Vector3f(6,0,0))


  start()

  def simpleInitApp {
    boxMat1 = new Material(assetManager,
      "Common/MatDefs/Misc/Unshaded.j3md")
    box1.setMaterial(boxMat1)
    rootNode.attachChild(box1)
    boxMat2 = new Material(assetManager,
      "Common/MatDefs/Misc/Unshaded.j3md")
    box2.setMaterial(boxMat2)
    rootNode.attachChild(box2)
    boxMat3 = new Material(assetManager,
      "Common/MatDefs/Misc/Unshaded.j3md")
    box3.setMaterial(boxMat3)
    rootNode.attachChild(box3)
  }

  override def simpleUpdate(tpf:Float) {
    time += tpf
    if(this.img1 == null) {
      img1 = getImg1()
    }
    if(img1 != null && this.img1.isCompleted) {
      val frame: Texture2D = new Texture2D(img1.value.get.get)
      boxMat1.setTexture("ColorMap", frame)
      this.img1 = getImg1()
    }
    if(this.img2 == null) {
      img2 = getImg2()
    }
    if(img2 != null && this.img2.isCompleted) {
      val frame: Texture2D = new Texture2D(img2.value.get.get)
      boxMat2.setTexture("ColorMap", frame)
      this.img2 = getImg2()
    }
    if(this.img3 == null) {
      img3 = getImg3()
    }
    if(img3 != null && this.img3.isCompleted) {
      val frame: Texture2D = new Texture2D(img3.value.get.get)
      boxMat3.setTexture("ColorMap", frame)
      this.img3 = getImg3()
    }
  }
}
