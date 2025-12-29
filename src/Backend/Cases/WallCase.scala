package Backend.Cases

import java.awt.Color

class WallCase(posX: Int, posY: Int, val WallColor: Color = Color.BLUE) extends Case(CaseType.Wall, posX, posY) {
  override def toString: String = "#"
}
