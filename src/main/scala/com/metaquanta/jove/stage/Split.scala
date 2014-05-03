package com.metaquanta.jove.stage

import org.opencv.core.{Rect, Mat}

/**
 * Created by matthew on 4/26/14.
 */
class Split extends Stage {
  // Split a frame down the middle. For 3D video streams where the images are
  // side by side.
  def getFrame(ins:List[Mat]):List[Mat] = {
    val in = ins.head
    val width = in.width
    val height = in.height
    // Output a pair of new ROI Mats
    List(
      new Mat(in, new Rect(0, 0, width / 2, height)),
      new Mat(in, new Rect(width / 2, 0, width / 2, height))
    )
  }
}
