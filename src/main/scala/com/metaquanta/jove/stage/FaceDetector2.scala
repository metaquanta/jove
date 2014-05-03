package com.metaquanta.jove.stage

/**
 * Created by matthew on 5/1/14.
 */
class FaceDetector2(cascadeFilename:String =
  "src/main/resources/lbpcascades/lbpcascade_frontalface.xml"
) extends CascadeClassifier(cascadeFilename)