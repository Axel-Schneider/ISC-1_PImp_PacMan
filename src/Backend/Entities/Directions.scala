package Backend.Entities

object Directions extends Enumeration {
  type Directions = Value;
  val Up, Down, Left, Right = Value;

  def getDeltaByDirection(directions: Directions): (Int, Int) = {
    (directions match {
      case Directions.Left => -1
      case Directions.Right => 1
      case _ => 0
    },
    directions match {
      case Directions.Up => -1
      case Directions.Down => 1
      case _ => 0
    })
  }
}
