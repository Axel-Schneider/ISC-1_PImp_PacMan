package Backend.Entities

import Backend.Entities.Directions.Directions
import Backend.global.Position

class Entity extends Position {
  protected var direction: Directions = Directions.Left
  protected var isAlive: Boolean = true;

  override def definePosition(x: Int, y: Int): Unit = super.definePosition(x, y)
  def Direction: Directions = direction;
  def IsAlive: Boolean = isAlive;
  def kill: Unit = { isAlive = false; }
  def revive: Unit = { isAlive = true; }
}
