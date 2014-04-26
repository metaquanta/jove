import org.opencv.calib3d.{StereoBM, StereoSGBM}
import org.opencv.core.{CvType, Mat}
import org.opencv.imgproc.Imgproc

/**
 * Created by matthew on 4/26/14.
 */
class StereoCorrespondence extends FramePipe {
  val SGBM = new StereoBM()
  def getFrame(ins:List[Mat]):List[Mat] = {
    //println("Processing Frame: " + ins(0) + "["+ins(0).`type`()+"]")
    val left = new Mat()
    val right = new Mat()
    Imgproc.cvtColor(ins(0), left, 7) //CV_RGB2GRAY    =7
    Imgproc.cvtColor(ins(1), right, 7)

    println("input:" + ins(0).`type` +":"+ins(0).channels)
    //ins(0).convertTo(left, CvType.CV_8UC1)
    //ins(1).convertTo(right, CvType.CV_8UC1)
    //println("new types: " + left.channels() + ", "+right.channels() + "["+CvType.CV_8UC1+"]")

    val toutput = new Mat() //Mat.zeros(ins(1).size(), CvType.CV_8UC1)
    SGBM.compute(left,right,toutput)

    val t2output = new Mat()
    toutput.convertTo(t2output, CvType.CV_16U)


    val t3output = new Mat()
    Imgproc.cvtColor(t2output, t3output, 8) // CV_GRAY2BGR    =8
    val output = new Mat()
    t3output.convertTo(output, 16)
    println("output:" + output.`type` +":"+output.channels)
    List(output)
  }
}
