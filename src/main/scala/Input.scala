import com.jme3.texture.Image
import java.nio.ByteBuffer
import org.opencv.core.{CvType, Mat, Core}
import org.opencv.highgui.VideoCapture
import org.opencv.imgproc.Imgproc
import scala.concurrent._
import ExecutionContext.Implicits.global

/**
 * Created by matthew on 4/25/14.
 */
class Input(ocvinput:VideoCapture) {
  // Initialize with the first frame (We need the size)
//  val (width, height) = () => {
//    val f:Mat = new Mat
//    ocvinput.read(f)
//    if (!f.empty) {
//      val s = f.size()
//      return (s.width, s.height)
//    }
//    return ((),())
//  }()
  // Why doesn't that work?
  val firstFrame = new Mat
  ocvinput.read(firstFrame)
  val width = firstFrame.width
  val height = firstFrame.height

  val flipmapx = new Mat(height, width, CvType.CV_32FC1)
  val flipmapy = new Mat(height, width, CvType.CV_32FC1)
  for(j <- 0 to height) {
    for (i <- 0 to width) {
      flipmapx.put(j,i, i)
      flipmapy.put(j,i, height - j)
    }
  }
  val flipdst = new Mat(firstFrame.size(), firstFrame.`type`())

  var nextFrame:Future[Mat] = future {
    val f:Mat = new Mat
    ocvinput.read(f)
    f
  }

  def getFrame():Future[Mat] = {
    if(nextFrame.isCompleted) {
      nextFrame = future {
        val f:Mat = new Mat
        ocvinput.read(f)
        f
      }
    }
    return nextFrame
  }

  def getImage():Future[Image] = {
    for {
      mat <- getFrame
      image <- future(mat2Image(mat))
    } yield (image)
  }

  def mat2Image(mat:Mat):Image = {
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
