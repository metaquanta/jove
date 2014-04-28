package com.metaquanta.jove

import org.opencv.core.Mat

/**
 * Created by matthew on 4/26/14.
 */
trait FramePipe {
  def getFrame(in:List[Mat]):List[Mat]
}
