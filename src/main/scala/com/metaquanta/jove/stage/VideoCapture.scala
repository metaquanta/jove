package com.metaquanta.jove.stage

import org.opencv.core.Mat
import org.opencv.highgui

/**
 * Created by matthew on 4/26/14.
 */
class VideoCapture extends highgui.VideoCapture with Stage {
  // A simple jove interface to opencv's VideoCapture

  // emulate java constructors
  def this(n:Int) {
    this()
    open(n)
  }
  def this(f:String) {
    this()
    open(f)
  }

  def getFrame(in:List[Mat]):List[Mat] = {
    val f:Mat = new Mat
    read(f)
    if(f.empty) {
      set(1, 0) //CV_CAP_PROP_POS_FRAMES=1
      read(f)
    }
    List(f)
  }
}
