import com.jme3.texture.Image
import java.nio.ByteBuffer
import org.opencv.core.Mat
import org.opencv.highgui.VideoCapture
import scala.concurrent._
import ExecutionContext.Implicits.global

/**
 * Created by matthew on 4/6/14.
 */
class Camera {
  val camera: VideoCapture = new VideoCapture(0)

  val video: VideoCapture = new VideoCapture("/Users/matthew/Desktop/test.mkv")

  val flipmapx:Mat = null
  val flipmapy:Mat = null

  camera.open(0)
  if (!camera.isOpened) {
    println("Camera Error")
  }
  else {
    println("Camera OK?")
  }

  def getFrame:Mat = {
    val image: Mat = new Mat
    camera.read(image)
    if (!image.empty) {
//      if (flipmapx == null) {
//        flipmapx = new Mat(image.size(), Image.Format.CV)
//        flipmapy = new Mat(image.size(), Mat.C)
//        for( int j = 0; j < src.rows; j++ )
//        { for( int i = 0; i < src.cols; i++ )
//          {
//              flipmapx.at<float>(j,i) = i ;
//              flipmapy.put(j,i, image.rows - j);
//           } // end of switch
//        }
//      }
      return image
    }
    return null
  }

  var frameFuture:Future[Mat] = null
  def getFrameFuture: Future[Mat] = {
    if (frameFuture == null || frameFuture.isCompleted) {
      frameFuture = future {
        getFrame
      }
    }
    frameFuture
  }

  def getFrame2:Mat = {
    val image: Mat = new Mat
    video.read(image)
    if (!image.empty) {
      return image
    }
    return null
  }

  var frameFuture2:Future[Mat] = null
  def getFrameFuture2: Future[Mat] = {
    if (frameFuture2 == null || frameFuture2.isCompleted) {
      frameFuture2 = future {
        getFrame2
      }
    }
    frameFuture2
  }

  def getImageFuture2():Future[Image] = {
    for {
      mat <- getFrameFuture2
      image <- future(mat2Image(mat))
    } yield (image)
  }


  //val getImageFuture: () => Future[Image] = () => {
  def getImageFuture():Future[Image] = {
    for {
      mat <- getFrameFuture
      image <- future(mat2Image(mat))
    } yield (image)
  }

  //val getMotionImageFuture: () => Future[Image] = () => {
  def getMotionImageFuture():Future[Image] = {
    for {
      mat <- getFrameFuture
      mat2 <- future(OpenCVExamples.update_mhi(mat))
      image <- future(mat2Image(mat2))
    } yield (image)
  }

  def mat2Image(mat:Mat):Image = {
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
