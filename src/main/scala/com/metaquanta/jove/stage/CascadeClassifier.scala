package com.metaquanta.jove.stage

import org.opencv.core._
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect

/**
 * Created by matthew on 4/28/14.
 */
abstract class CascadeClassifier(cascadeFilename:String) extends Stage {
  val faceCascade = new objdetect.CascadeClassifier()
  println("CascadeClassifier.load(" + cascadeFilename + "): "
    + faceCascade.load(cascadeFilename)
  )

  def getFrame(ins:List[Mat]):List[Mat] = {
    val input = ins(0)

    val inGrey = new Mat()
    Imgproc.cvtColor(input, inGrey, 7) // CV_RGB2GRAY = 7
    Imgproc.equalizeHist(inGrey, inGrey)

    val faces = new MatOfRect()
    faceCascade.detectMultiScale(inGrey, faces)

    val facesAr = faces.toArray

    if(facesAr.size > 0) {
      val center = new Point(facesAr(0).x + facesAr(0).width * 0.5,
        facesAr(0).y + facesAr(0).height * 0.5
      )
      Core.ellipse(input, center,
        new Size(facesAr(0).width * 0.5, facesAr(0).height * 0.5), 0, 0, 360,
        new Scalar(255, 0, 255), 4, 8, 0
      )
    }

    List(input)
  }
}