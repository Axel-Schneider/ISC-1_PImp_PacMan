package Backend.Cases

import java.awt.Color

class WallCase(val WallColor: Color = Color.BLUE) extends Case(CaseType.Wall) {
  override def toString: String = "#"

}
