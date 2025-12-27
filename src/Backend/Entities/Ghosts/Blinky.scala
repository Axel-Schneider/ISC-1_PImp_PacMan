package Backend.Entities.Ghosts

import Backend.Cases.Case

import java.awt.Color

class Blinky extends Ghosts(Color.RED) {
  override def takeDecision(map: Array[Array[Case]]): Unit = {
    // To implement AI motion
  }

  override def toString: String = "Blinky"
}

object Blinky {
  val INSTANCE = new Blinky();
}
