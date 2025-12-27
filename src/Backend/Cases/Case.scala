package Backend.Cases

import Backend.Cases.CaseType.CaseType
import Backend.global.Position

class Case(val CaseType: CaseType) extends Position {
  override def definePosition(x: Int, y: Int): Unit = super.definePosition(x, y)
}