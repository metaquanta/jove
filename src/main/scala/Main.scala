import com.jme3.math.Vector3f
import org.opencv.highgui.VideoCapture

/**
 * Created by matthew on 4/25/14.
 */
object Main extends App {
  println("loading opencv...")
  System.loadLibrary("opencv_java248")
  println("loading JME3...")
  val jme3app = new JME3Application()
  jme3app.synchronized {
    println("waiting...")
    jme3app.wait()
  }
  jme3app.attachScreen(
    new Screen(
      new Input(new VideoCapture("/Users/matthew/Desktop/test.mkv")), jme3app),
    new Vector3f(2,0,0))
}