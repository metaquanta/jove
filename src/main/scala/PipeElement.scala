import com.jme3.texture.Image
import java.nio.ByteBuffer
import org.opencv.core.{CvType, Mat, Core}
import org.opencv.highgui.{VideoCapture, Highgui}
import org.opencv.imgproc.Imgproc
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

/**
 * Created by matthew on 4/25/14.
 */
class PipeElement(cap:FramePipe, in:List[PipeElement]) {
  //val flipdst = new Mat(firstFrame.size(), firstFrame.`type`())
  //var flipdst:Mat = null

  var nextFrame:Future[List[Mat]] = null

  // This should only be called when a frame is available
  // All frames should be same size
  lazy val flipdst = {
    Await.ready(nextFrame, 0 nanos)
    val f=nextFrame.value.get.get.head
    new Mat(f.size(), f.`type`())
  }

  def getFrame():Future[List[Mat]] = {
    if(nextFrame == null || nextFrame.isCompleted) {
      val inmats = in.map(x => x.getFrame)
      nextFrame = for {
        mats <- Future.sequence(inmats)
        f <- future(cap.getFrame(mats.flatten))
      } yield f
    }
    return nextFrame
  }

  def getImage():Future[List[Image]] = {
    for {
      mats <- getFrame
      image <- future(mats.map(x => mat2Image(x)))
    } yield (image)
  }

  def mat2Image(mat:Mat):Image = {
    //println("Rendering Frame " + mat)

    // Remap the mat
    // This is really slow, I don't know why
    //Imgproc.remap( mat, flipdst, flipmapx, flipmapy, 1) //Imgproc.CV_INTER_LINEAR = 1 and is private for some reason
    Core.flip(mat, flipdst, 0)

    // Perform the voodoo magic
    val width: Int = flipdst.width
    val height: Int = flipdst.height
    val buffChannels: Int = 3
    val len: Int = width * height * buffChannels
    val byteBuff: ByteBuffer = ByteBuffer.allocateDirect(len)
    val bytes: Array[Byte] = new Array[Byte](len)
    flipdst.get(0, 0, bytes)
    new Image(Image.Format.BGR8, width, height, byteBuff.put(bytes))
  }

}
