package com.metaquanta.jove.stage

import org.opencv.core.Mat

/**
 * Created by matthew on 4/26/14.
 */
trait Stage {
  // This is the jove interface
  def getFrame(in:List[Mat]):List[Mat]
}
