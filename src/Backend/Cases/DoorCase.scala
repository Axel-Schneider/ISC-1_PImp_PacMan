package Backend.Cases

import java.awt.Color

class DoorCase(posX: Int, posY: Int) extends Case(CaseType.Door, posX, posY) {
  val WallColor: Color = Color.PINK;

  override def toString: String = "#";
}
