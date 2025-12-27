package Backend.Cases

import java.awt.Color

class DoorCase extends Case(CaseType.Door) {
  val WallColor: Color = Color.PINK;

  override def toString: String = "#";
}
