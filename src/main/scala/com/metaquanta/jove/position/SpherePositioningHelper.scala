package com.metaquanta.jove.position

import com.jme3.math.{FastMath, Quaternion, Vector3f}

/**
 * Created by matthew on 4/29/14.
 */
class SpherePositioningHelper(col:Int, row:Int) extends Position {
  var radius = 5f/FastMath.PI
  val cols = 16f
  val rows = 14f

  // This is complicated (just excessive composition of simple trig): Position
  // Quads on a sphere, with "rows" rows and "cols" columns.
  // Moving the viewport around from the center of the sphere lets any Quad
  // seem to face you
  def position(w:Float, h:Float):Vector3f = {
    FastMath.sphericalToCartesian(
      new Vector3f(radius, col*2f*FastMath.PI/cols, row*2f*FastMath.PI/rows),
      new Vector3f
    )
      .mult(radius)
      .subtract(orientation(w,h).mult(new Vector3f(w/2, h/2, 0f)))
  }

  def orientation(w:Float, h:Float):Quaternion = {
    (new Quaternion())
      .fromAngleAxis(-FastMath.PI/2, Vector3f.UNIT_Y)
      .mult(
    (new Quaternion())
      .fromAngleAxis(col*(-2f)*FastMath.PI/cols, Vector3f.UNIT_Y))
      .mult(
    (new  Quaternion())
      .fromAngleAxis(row*(2f)*FastMath.PI/rows, Vector3f.UNIT_X))
  }
}
