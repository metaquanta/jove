import com.jme3.texture.Image
import java.nio.ByteBuffer
import org.opencv.core.Mat
import org.opencv.highgui.VideoCapture
import scala.concurrent._
import ExecutionContext.Implicits.global

/**
 * Created by matthew on 4/25/14.
 */
class Input(ocvinput:VideoCapture) {
  // Initialize with the first frame
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
  val size = firstFrame.size()

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
    // Perform the voodoo magic
    val width: Int = mat.width
    val height: Int = mat.height
    val buffChannels: Int = 3
    val len: Int = width * height * buffChannels
    val byteBuff: ByteBuffer = ByteBuffer.allocateDirect(len)
    val bytes: Array[Byte] = new Array[Byte](len)
    mat.get(0, 0, bytes)
    new Image(Image.Format.BGR8, width, height, byteBuff.put(bytes))
  }

}
