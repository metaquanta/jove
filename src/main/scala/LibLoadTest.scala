import com.jme3.texture.Image
import scala.concurrent._
import ExecutionContext.Implicits.global

/**
 * Created by matthew on 4/6/14.
 */
object LibLoadTest extends App {
  println("opencv test...")
  System.loadLibrary("opencv_java248")
  val camera = new Camera()
  println("jME3 test...")
  val jme3app = new jME3App()

  jme3app.getImg1 = camera.getImageFuture
  jme3app.getImg2 = camera.getMotionImageFuture

  jme3app.getImg3 = camera.getImageFuture2
//  jme3app.getImg = () => {
//    future {
//      camera.getImage
//    }
//  }
}
