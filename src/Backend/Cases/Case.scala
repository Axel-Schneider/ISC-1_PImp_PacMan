package Backend.Cases

import Backend.Cases.CaseType.CaseType
import Backend.Entities.Entity
import Backend.global.Position

import scala.collection.mutable.ArrayBuffer

class Case(val CaseType: CaseType, posX: Int, posY: Int) extends Position {
  override def definePosition(x: Int, y: Int): Unit = super.definePosition(x, y)
  var Entities: ArrayBuffer[Entity] = ArrayBuffer.empty;
  definePosition(posX, posY)
}