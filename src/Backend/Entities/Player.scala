package Backend.Entities

import Backend.Cases.{CaseType, RoadCase}
import Backend.Entities.Directions.Directions
import Backend.Logical

import scala.util.Random

class Player extends Entity {
  protected var score: Int = 0;

  def Score: Int = score;

  def ChangeDirection(direction: Directions): Unit = {
    this.direction = direction
  }
  def addScore(points: Int): Unit = {
    score += points;
  }
  def resetScore(): Unit = {
    score = 0;
  }
  override def toString: String = "Player"


  // LAB
  def LAB_AIPlayerChoose(logical: Logical): Unit = {

    println(s"$this taking decision...")
    if(!logical.IsPointInTheMap(x, y)) return;
    val currentCase = logical.Map(y)(x);
    if(!currentCase.isInstanceOf[RoadCase]) return;
    val currentRoad = currentCase.asInstanceOf[RoadCase];
    val (dtx, dty) = Directions.getDeltaByDirection(Direction)
    val (ntx, nty) = logical.CorrectPoint(x + dtx, y + dty);
    if(logical.Map(nty)(ntx).CaseType == CaseType.Road && !currentRoad.IsIntersection) return;

    var dir: Directions = Directions.Right
    var ny: Int = -1
    var nx: Int = -1
    val (dx, dy) = Directions.getDeltaByDirection(Direction)
    val (lx, ly) = (x-dx, y-dy)
    do {
      dir = Directions(Random.nextInt(Directions.maxId))
      val (deltaX, deltaY) = Directions.getDeltaByDirection(dir);
      ny = y + deltaY;
      nx = x + deltaX;
    } while (
      !logical.IsPointInTheMap(nx, ny) ||
        (lx == nx && ly == ny) ||
        logical.Map(ny)(nx).CaseType != CaseType.Road
    )
    direction = dir;

    println(s"$this taked decision $direction")
  }
}
