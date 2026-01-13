package Backend.Entities.Ghosts

import Backend.Logical

import java.awt.Color
import scala.math.{pow, random, sqrt}
import scala.util.Random

class Clyde extends Ghosts(Color.ORANGE) {
  override def toString: String = "Clyde"

  // Targets directly the player if Player is near Clyde
  override def getTarget(logical: Logical): (Int, Int) = {
    val dist = sqrt(pow(logical.Player.X - this.X, 2) + pow(logical.Player.Y - this.Y, 2))
    if (dist < 3){
      (logical.Player.X, logical.Player.Y)
    } else {
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
}

object Clyde {
  val INSTANCE = new Clyde();
}
