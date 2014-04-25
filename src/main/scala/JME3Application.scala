import com.jme3.app.SimpleApplication
import com.jme3.math.Vector3f

/**
 * Created by matthew on 4/25/14.
 */
class JME3Application extends SimpleApplication {
  start()

  var screens = List[Screen]()

  def simpleInitApp() {
    synchronized {
      println("notifying...")
      notify()
    }
  }

  override def simpleUpdate(tpf:Float) {
    screens.foreach(s => s.update())
  }

  def attachScreen(s:Screen, p:Vector3f) {
    s.setLocalTranslation(p)
    rootNode.attachChild(s)
    screens = screens:+s
  }
}
