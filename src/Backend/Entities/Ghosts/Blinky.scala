package Backend.Entities.Ghosts

import Backend.Logical

import java.awt.Color

class Blinky extends Ghosts(Color.RED) {
  override def toString: String = "Blinky"

  // Targets directly the player
  override def getTarget(logical: Logical): (Int, Int) = {
    (logical.Player.X, logical.Player.Y)
  }
}

object Blinky {
  val INSTANCE = new Blinky();
}
