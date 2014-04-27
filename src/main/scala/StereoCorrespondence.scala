import org.opencv.calib3d.{StereoBM, StereoSGBM}
import org.opencv.core.{CvType, Mat}
import org.opencv.imgproc.Imgproc

/**
 * Created by matthew on 4/26/14.
 */
class StereoCorrespondence extends FramePipe {
  val SGBM = new StereoBM()
  def getFrame(ins:List[Mat]):List[Mat] = {
    val left = new Mat()
    val right = new Mat()

    // OMG! The HORROR that is Mat type conversions...
    // ...convert to grey scale
    Imgproc.cvtColor(ins(0), left, 7) //CV_RGB2GRAY    =7
    Imgproc.cvtColor(ins(1), right, 7)

    val depthmap = new Mat()
    SGBM.compute(left,right,depthmap)

    // OMG! The HORROR continues...
    val t = new Mat()
    // ...convert to unsigned (from 16S)
    depthmap.convertTo(t, CvType.CV_16U)
    // ...convert back to color
    Imgproc.cvtColor(t, depthmap, 8) // CV_GRAY2BGR    =8
    // ...convert to who the fuck knows?
    depthmap.convertTo(t, 16) // 16=???
    List(t)
  }
}
