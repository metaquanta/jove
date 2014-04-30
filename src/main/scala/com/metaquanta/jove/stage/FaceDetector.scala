package com.metaquanta.jove.stage

import org.opencv.core._
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier

/**
 * Created by matthew on 4/28/14.
 */
class FaceDetector  extends Stage {
  val faceCascade = new CascadeClassifier()
  println("CascadeClassifier.load(haarcascade_frontalface): " + faceCascade.load("src/main/resources/haarcascades/haarcascade_frontalface_alt.xml"))

  def getFrame(ins:List[Mat]):List[Mat] = {
    val input = ins(0)

    val inGrey = new Mat()
    // OMG! The HORROR that is Mat type conversions...
    // ...convert to grey scale
    Imgproc.cvtColor(input, inGrey, 7) //CV_RGB2GRAY    =7
    Imgproc.equalizeHist( inGrey, inGrey )

    val faces = new MatOfRect()
    faceCascade.detectMultiScale( inGrey, faces)//, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30) )

    val facesAr = faces.toArray()

    if(facesAr.size > 0) {
      //println("FOUND A FACE!")
      val center = new Point( facesAr(0).x + facesAr(0).width*0.5, facesAr(0).y + facesAr(0).height*0.5 )
      Core.ellipse( input, center, new Size( facesAr(0).width*0.5, facesAr(0).height*0.5), 0, 0, 360, new Scalar( 255, 0, 255 ), 4, 8, 0 );
    }

    List(input)
  }
}