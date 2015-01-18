package com.metaquanta.jove.examples

import com.metaquanta.jove._
import com.metaquanta.jove.stage._
import com.metaquanta.jove.visualization.ScreenVisualizer
import com.metaquanta.jove.position.SpherePositioningHelper
import com.metaquanta.opencvexamples.MotionExampleJava
import org.opencv.core.Mat

/**
 * Created by matthew on 4/25/14.
 */
object FaceDetectExample extends App {
  // Begin boilerplate
  print("loading OpenCV...")
  System.loadLibrary("opencv_java2410")
  println("...done")
  print("loading JME3...")
  val jme3app = new JME3Application()
  jme3app.synchronized {
    jme3app.wait()
  }
  // End boilerplate

  val process = new Jove(jme3app)

  val camera = process.addStage(new VideoCapture(0))

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(0, -1), jme3app), camera(0)
  )

  // This is an example of using a pure Java OpenCV class that is completely
  // agnostic of jove.
  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(0, 1), jme3app),
    // "with Stage" adds the jove interface and requires the one simple
    // getFrame() declaration
    process.addStage(
      new MotionExampleJava() with Stage {
        def getFrame(in:List[Mat]):List[Mat] = {
          List(update_mhi(in.head))
        }
      },
      camera(0),
      "Motion"
    )(0)
  )

  // Add a collection of face detectors
  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(-2, 0), jme3app),
    process.addStage(new FaceDetector1(), camera(0), "FaceDetector")(0)
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(0, 0), jme3app),
    process.addStage(new FaceDetector2(), camera(0), "FaceDetector2")(0)
  )

  process.addVisualizer(
    new ScreenVisualizer(new SpherePositioningHelper(2, 0), jme3app),
    process.addStage(new HeadDetector(), camera(0), "HeadDetector")(0)
  )
}
