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

  val video = new PipeElement(new VideoCapturePipe("/Users/matthew/Desktop/test.mkv"), List())

//  jme3app.attachScreen(
//    new Screen(video, 0, jme3app),
//    new Vector3f(0,0,0))

//  jme3app.attachScreen(
//    new Screen(new PipeElement(new Motion(), List(video)), 0, jme3app),
//    new Vector3f(-3,0,0)
//  )

  val split = new PipeElement(new Split(), List(video))

  val stereo = new PipeElement(new StereoCorrespondence(),List(split))
//
//  jme3app.attachScreen(
//    new Screen(split, 0, jme3app),
//    new Vector3f(-2,0,0)
//  )
//
  jme3app.attachScreen(
    new Screen(split, 1, jme3app),
    new Vector3f(0,1,0)
  )

  jme3app.attachScreen(
    new Screen(stereo, 0, jme3app),
    new Vector3f(0,2,0)
  )

  jme3app.attachDepthMap(
    new DepthMap(stereo, 0, jme3app),
    new Vector3f(0,0,0)
  )



  //  jme3app.attachScreen(
//    new Screen(
//      new PipeElement(new VideoCapturePipe(0), List()), jme3app),
//    new Vector3f(-2,0,0))
}
