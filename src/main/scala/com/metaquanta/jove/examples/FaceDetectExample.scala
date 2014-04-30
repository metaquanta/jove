package com.metaquanta.jove.examples

import com.metaquanta.jove._
import com.jme3.math.Vector3f
import com.metaquanta.jove.cvstageexamples.{FaceDetector, Motion}
import com.metaquanta.jove.visualization.ScreenVisualizer

/**
 * Created by matthew on 4/25/14.
 */
object FaceDetectExample extends App {
  println("loading opencv...")
  System.loadLibrary("opencv_java248")
  println("loading JME3...")
  val jme3app = new JME3Application()
  jme3app.synchronized {
    println("waiting...")
    jme3app.wait()
  }

//  val video = new PipeElement(new VideoCapturePipe("/Users/matthew/Desktop/test.mkv"), List())
//
////  jme3app.attachScreen(
////    new ScreenVisualizer(video, 0, jme3app),
////    new Vector3f(0,0,0))
//
//  val split = new PipeElement(new Split(), List(video))
//
//  val stereo = new PipeElement(new StereoCorrespondence(),List(split))
////
////  jme3app.attachScreen(
////    new ScreenVisualizer(split, 0, jme3app),
////    new Vector3f(-2,0,0)
////  )
////
//  jme3app.attachScreen(
//    new ScreenVisualizer(split, 1, jme3app),
//    new Vector3f(0,1,0)
//  )
//
//  jme3app.attachScreen(
//    new ScreenVisualizer(stereo, 0, jme3app),
//    new Vector3f(0,2,0)
//  )
//
//  jme3app.attachDepthMap(
//    new DepthMapVisualizer(stereo, 0, jme3app),
//    new Vector3f(0,0,0)
//  )

  val camera = new PipeElement(new VideoCapturePipe(0), List())

//  jme3app.attachVisualizer(
//    new ScreenVisualizer("Camera", camera, 0, jme3app, new Vector3f(2,0,0))
//  )
//
//  jme3app.attachVisualizer(
//    new ScreenVisualizer("Motion", new PipeElement(new Motion(), List(camera)), 0, jme3app,
//      new Vector3f(-2,0,0))
//  )
//
//  jme3app.attachVisualizer(
//    new ScreenVisualizer("Face Detection", new PipeElement(new FaceDetector(), List(camera)), 0, jme3app,
//      new Vector3f(-4,0,0))
//  )


}
