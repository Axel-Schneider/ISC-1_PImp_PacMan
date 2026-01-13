package Backend.Entities.Ghosts

import Backend.Logical

import java.awt.Color
import scala.math.{pow, sqrt}
import scala.util.Random

class Inky extends Ghosts(Color.BLUE) {
  override def toString: String = "Inky"

  // Targets random point in the Map
  override def getTarget(logical: Logical): (Int, Int) = {
    var x = 0
    var y = 0
    var attempts = 0

    do {
      x = Random.nextInt(logical.Map(0).length)
      y = Random.nextInt(logical.Map.length)
      attempts += 1
    } while (!logical.IsPointInTheMap(x, y) && attempts < 10)

    (x, y)
  }
}

object Inky {
  val INSTANCE = new Inky();
}