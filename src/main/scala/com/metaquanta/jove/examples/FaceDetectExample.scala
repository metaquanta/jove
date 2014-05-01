package com.metaquanta.jove.examples

import com.metaquanta.jove._
import com.jme3.math.Vector3f
import com.metaquanta.jove.stage._
import com.metaquanta.jove.visualization.ScreenVisualizer
import com.metaquanta.jove.position.SpherePositioningHelper
import com.metaquanta.opencvexamples.MotionExampleJava
import org.opencv.core.Mat

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

  val process = new Jove(jme3app)

  val camera = process.addStage(new VideoCapture(0))

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(0,-1), jme3app), camera(0)
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(0,1), jme3app),
    process.addStage(new MotionExampleJava() with Stage {
      def getFrame(in:List[Mat]):List[Mat] = {
        List(update_mhi(in.head))
      }
    }, camera(0), "Motion")(0)
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(-2,0), jme3app),
    process.addStage(new FaceDetector(), camera(0), "FaceDetector")(0)
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(0,0), jme3app),
    process.addStage(new FaceDetector2(), camera(0), "FaceDetector2")(0)
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(2,0), jme3app),
    process.addStage(new HeadDetector(), camera(0), "HeadDetector")(0)
  )
}
