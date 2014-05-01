package com.metaquanta.jove.visualization

import com.jme3.texture.Image

/**
 * Created by matthew on 4/30/14.
 */
trait ImageStream {
  def ready:Boolean
  def next:Image
}
