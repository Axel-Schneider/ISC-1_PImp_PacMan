package Backend.Entities.Ghosts

import Backend.Cases.{CaseType, RoadCase}
import Backend.Entities.Directions.Directions
import Backend.Entities.{Directions, Entity}
import Backend.Logical

import java.awt.Color
import java.util.concurrent.{ScheduledFuture, ScheduledThreadPoolExecutor, TimeUnit}
import scala.util.Random

abstract class Ghosts(val MainColor: Color) extends Entity {
  direction = Directions.Up

  protected var isVulnerable: Boolean = false;
  protected var isBlinking: Boolean = false;

  def IsVulnerable = isVulnerable;
  def IsBlinking = isBlinking;

  private val makeBlinkThreadExecutor: ScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
  private var currentMakeBlinkTask: Option[ScheduledFuture[_]] = None;
  private val makeBlinkTask = new Runnable {
    override def run(): Unit = {
      if(IsVulnerable)
        isBlinking = true
    }
  }
  private val resetVulnerableThreadExecutor: ScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
  private var currentResetVulnerabilityTask: Option[ScheduledFuture[_]] = None;
  private val resetVulnerabilityTask = new Runnable {
    override def run(): Unit = {
      resetVulnerability();
    }
  }



  def makeVulnerable(): Unit = {
    // TO DO : Dynamic calcul timing of vulnerability
    if(isVulnerable) {
      if(currentResetVulnerabilityTask.isDefined) currentResetVulnerabilityTask.get.cancel(true)
      if(currentMakeBlinkTask.isDefined) currentMakeBlinkTask.get.cancel(true)
    }
    isVulnerable = true;
    isBlinking = false;
    currentMakeBlinkTask = Some(makeBlinkThreadExecutor.schedule(makeBlinkTask, 5, TimeUnit.SECONDS))
    currentResetVulnerabilityTask = Some(resetVulnerableThreadExecutor.schedule(resetVulnerabilityTask, 10, TimeUnit.SECONDS))
  }

  def resetVulnerability(): Unit = {
    isVulnerable = false;
    isBlinking = false;
  }

  protected var isLastCaseDoor = false;
  def takeDecision(logical: Logical): Unit = {
    // DEFAULT MOVEMENT (RANDOM)

    println(s"$this taking decision...")
    if(!logical.IsPointInTheMap(x, y)) return;
    val currentCase = logical.Map(y)(x);
    val isLastADoor = isLastCaseDoor
    isLastCaseDoor = currentCase.CaseType == CaseType.Door
    if(isLastCaseDoor) return;
    if(!currentCase.isInstanceOf[RoadCase]) return;
    val currentRoad = currentCase.asInstanceOf[RoadCase];
    if(!isLastADoor && !currentRoad.IsIntersection && !currentRoad.isGhostsSpawn) return;

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
      !(
        logical.Map(ny)(nx).CaseType == CaseType.Road ||
        logical.Map(ny)(nx).CaseType == CaseType.Door &&
        currentRoad.isGhostsSpawn
      )
    )
    direction = dir;

    println(s"$this taked decision $direction")
  };
}