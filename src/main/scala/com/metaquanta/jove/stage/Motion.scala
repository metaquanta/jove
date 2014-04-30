package com.metaquanta.jove.stage

import org.opencv.core.Mat
import com.metaquanta.jove._
import com.metaquanta.opencvexamples.MotionExampleJava

/**
 * Created by matthew on 4/26/14.
 */
class Motion extends Stage {

  val MotionDetector = new MotionExampleJava

  def getFrame(in:List[Mat]):List[Mat] = {
    List(MotionDetector.update_mhi(in.head))
  }
}