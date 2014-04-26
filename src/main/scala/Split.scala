import org.opencv.core.{Rect, Mat}

/**
 * Created by matthew on 4/26/14.
 */
class Split extends FramePipe {
  def getFrame(ins:List[Mat]):List[Mat] = {
    val in = ins.head
    val width = in.width
    val height = in.height
    List(new Mat(in, new Rect(0,0,width/2, height)), new Mat(in, new Rect(width/2,0,width/2, height)))
  }
}
