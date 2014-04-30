package com.metaquanta.jove.position

import com.jme3.math.{Quaternion, Vector3f}

/**
 * Created by matthew on 4/29/14.
 */
trait Position {
  def position(w:Float, h:Float):Vector3f
  def orientation(w:Float, h:Float):Quaternion
}
