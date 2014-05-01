package com.metaquanta.jove.examples

import com.metaquanta.jove.{Jove, JME3Application}
import com.metaquanta.jove.visualization.{Visualizer, DepthMapVisualizer, ScreenVisualizer}
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

  val video = process.addStage(
    new VideoCapture("/Users/matthew/Desktop/test.mkv")
  )

  val split = process.addStage(new Split(), video(0), "Split")

  val stereo = process.addStage(
    new StereoCorrespondence(), List(split(0), split(1)), "DepthMap"
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(0,-1), jme3app), video(0)
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(-1,0), jme3app), split(0)
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(1,0), jme3app), split(1)
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(0,1), jme3app), stereo(0)
  )

  process.addVisualizer(
    new DepthMapVisualizer(new SpherePositioningHelper(0,0), jme3app),
    stereo(0)
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(0,0), jme3app), split(0)
  )



}
