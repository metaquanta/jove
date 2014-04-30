package com.metaquanta.jove.examples

import com.metaquanta.jove.{VideoCapturePipe, PipeElement, JME3Application}
import com.metaquanta.jove.visualization.{DepthMapVisualizer, ScreenVisualizer}
import com.jme3.math.Vector3f
import com.metaquanta.jove.cvstageexamples.{StereoCorrespondence, Split}
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

  val video = new PipeElement(new VideoCapturePipe("/Users/matthew/Desktop/test.mkv"), List())

  val split = new PipeElement(new Split(), List(video))

  val stereo = new PipeElement(new StereoCorrespondence(),List(split))

  jme3app.attachVisualizer(
    new ScreenVisualizer("Video", video, 0, jme3app, new SpherePositioningHelper(0,-1))
  )

  jme3app.attachVisualizer(
    new ScreenVisualizer("Left", split, 0, jme3app, new SpherePositioningHelper(-1,0))
  )

  jme3app.attachVisualizer(
    new ScreenVisualizer("Right", split, 1, jme3app, new SpherePositioningHelper(1,0))
  )

  jme3app.attachVisualizer(
    new ScreenVisualizer("DepthMap", stereo, 0, jme3app, new SpherePositioningHelper(0,1))
  )

  jme3app.attachVisualizer(
    new DepthMapVisualizer("DepthMap3D", stereo, 0, jme3app, new SpherePositioningHelper(0,0))
  )

  jme3app.attachVisualizer(
    new ScreenVisualizer("Leftp", split, 0, jme3app, new SpherePositioningHelper(0,0))
  )



}
