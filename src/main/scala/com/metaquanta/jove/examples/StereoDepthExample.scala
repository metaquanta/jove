package com.metaquanta.jove.examples

import com.metaquanta.jove.{Jove, JME3Application}
import com.metaquanta.jove.visualization.{DepthMapVisualizer, ScreenVisualizer}
import com.jme3.math.Vector3f
import com.metaquanta.jove.stage.{VideoCapture, StereoCorrespondence, Split}
import com.metaquanta.jove.position.SpherePositioningHelper

/**
 * Created by matthew on 4/25/14.
 */
object StereoDepthExample extends App {
  println("loading opencv...")
  System.loadLibrary("opencv_java248")
  println("loading JME3...")
  val jme3app = new JME3Application()
  jme3app.synchronized {
    println("waiting...")
    jme3app.wait()
  }

  val process = new Jove(jme3app)

  val video = process.addStage("video", new VideoCapture("/Users/matthew/Desktop/test.mkv"))

//  val split = process.addStage("splitter", new Split(), video(0))
//
//  val stereo = process.addStage("depthMap", new StereoCorrespondence(), List(split(0), split(1)))

  process.addVisualizer(new ScreenVisualizer(new SpherePositioningHelper(0,-1), jme3app), video(0))
//
//  jme3app.attachVisualizer(
//    new ScreenVisualizer("Video", video, 0, jme3app, new SpherePositioningHelper(0,-1))
//  )
//
//  jme3app.attachVisualizer(
//    new ScreenVisualizer("Left", split, 0, jme3app, new SpherePositioningHelper(-1,0))
//  )
//
//  jme3app.attachVisualizer(
//    new ScreenVisualizer("Right", split, 1, jme3app, new SpherePositioningHelper(1,0))
//  )
//
//  jme3app.attachVisualizer(
//    new ScreenVisualizer("DepthMap", stereo, 0, jme3app, new SpherePositioningHelper(0,1))
//  )
//
//  jme3app.attachVisualizer(
//    new DepthMapVisualizer("DepthMap3D", stereo, 0, jme3app, new SpherePositioningHelper(0,0))
//  )
//
//  jme3app.attachVisualizer(
//    new ScreenVisualizer("Leftp", split, 0, jme3app, new SpherePositioningHelper(0,0))
//  )
//


}
