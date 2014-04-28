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
  var nextFrame:Future[List[Mat]] = null

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
    // Remap the mat
    // This is really slow, I don't know why
    //Imgproc.remap( mat, flipdst, flipmapx, flipmapy, 1) //Imgproc.CV_INTER_LINEAR = 1 and is private for some reason
    val flipdst = new Mat
    Core.flip(mat, flipdst, 0)

    // Perform the voodoo magic
    val width: Int = flipdst.width
    val height: Int = flipdst.height
    val channels: Int = flipdst.channels()
    val len: Int = width * height * channels
    val byteBuff: ByteBuffer = ByteBuffer.allocateDirect(len)
    val bytes: Array[Byte] = new Array[Byte](len)
    flipdst.get(0, 0, bytes)
    new Image(Image.Format.BGR8, width, height, byteBuff.put(bytes))
  }

}
