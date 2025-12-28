package Backend

import Backend.Cases.{Case, CaseType, DoorCase, EmptyCase, Items, RoadCase, WallCase}
import Backend.Entities.Directions.Directions
import Backend.Entities.Ghosts.{Blinky, Clyde, Ghosts, Inky, Pinky}
import Backend.Entities.{Directions, Entity, Player}

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Logical {
  private var map: Array[Array[Case]] = Array.empty;
  private val player = new Player;
  private val ghosts = Array(
    Blinky.INSTANCE,
    Clyde.INSTANCE,
    Inky.INSTANCE,
    Pinky.INSTANCE
  );

  private var isGamePlaying = false;

  private var playerSpawn: RoadCase = null;
  private var ghostsSpawn: Array[RoadCase] = Array.empty;
  private var itemsSpawn: RoadCase = null;

  private val subscriptions: ArrayBuffer[Logical => Unit] = ArrayBuffer.empty;
  private val ex: ScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
  private val task = new Runnable {
    override def run(): Unit = {
      notifyListener()
    }
  }
  private val infiniteNotifier = ex.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS)

  def Map = map;
  def Player = player;
  def Ghosts: Array[Ghosts] = ghosts;

  def IsGamePlaying = isGamePlaying;

  def subscribeCycle(callback: Logical => Unit): Unit = {
    subscriptions += callback;
  }

  def notifyListener(): Unit = {
    for(s <- subscriptions) s(this)
  }

  def startGame() = {
    isGamePlaying = true;
  }

  def pauseGame() = {
    isGamePlaying = false;
  }

  def LoadLevel(map: Array[String]): Unit = {
    this.map = Array.ofDim(map.length, map(0).length);

    for((l, y) <- map.zipWithIndex) {
      for((c, x) <- l.zipWithIndex) {
        this.map(y)(x) = c match {
          case ' ' => new EmptyCase(x, y);
          case 'w' => new WallCase(x, y);
          case 'r' => new RoadCase(x, y);
          case 'd' => {
            val d = new RoadCase(x, y);
            d.Item = Items.PacDot;
            d
          };
          case 'D' => {
            val D = new RoadCase(x, y);
            D.Item = Items.PowerPellet;
            D
          };
          case 'i' => {
            val i = new RoadCase(x, y)
            itemsSpawn = i;
            i
          };
          case 'v' => new DoorCase(x, y);
          case 'P' => {
            val P = new RoadCase(x, y);
            playerSpawn = P;
            P
          };
          case 'G' => {
            val G = new RoadCase(x, y)
            ghostsSpawn :+= G;
            G
          };
        }
      }
    }

    if(player == null) throw new Exception("No spawn for the player in the level!")
    playerSpawn.Entities += player;
    player.definePosition(playerSpawn.X, playerSpawn.Y)

    if(ghostsSpawn.length <= 0) throw new Exception("No spawn for the ghosts in the level !")
    for(g <- ghosts) {
      val spw = ghostsSpawn(Random.nextInt(ghostsSpawn.length));
      spw.Entities += g;
      g.definePosition(spw.X, spw.Y)
    }
  }

  def ChangePlayerDirection(direction: Directions): Unit = {
    val (deltaX, deltaY) = Directions.getDeltaByDirection(direction);
    val nextCase = map(player.Y + deltaY)(player.X + deltaX)
    if(nextCase.CaseType == CaseType.Road) player.ChangeDirection(direction)
    else println("Cannot change direction for a wall")
  }

  subscriptions += calculateFrame;
  private def calculateFrame(logical: Logical): Unit = {
    moveEntity(Player)
    ghosts.foreach(g => moveEntity(g, true))
    eatCaseByPlayer()

    // LAB
    ChangePlayerDirection(Directions(Random.nextInt(Directions.maxId)));
  }

  private def moveEntity(entity: Entity, isGhosts: Boolean = false): Unit = {
    if(!isGamePlaying) return;
    val (deltaX, deltaY) = Directions.getDeltaByDirection(entity.Direction);

    val nextCase = map(entity.Y + deltaY)(entity.X + deltaX)
    val currentCase = map(entity.Y)(entity.X);

    if(nextCase.CaseType == CaseType.Road || (isGhosts && nextCase.CaseType == CaseType.Door)) {
      val nextRoad = nextCase;
      currentCase.Entities.remove(currentCase.Entities.indexOf(entity));
      nextRoad.Entities += entity;
      nextRoad.definePositionOf(entity)
    } else {
      println(s"$entity can't go forward, next case not a road")
    }
  }

  private def eatCaseByPlayer() {
    if(!isGamePlaying) return;
    val currentCase = map(player.Y)(player.X);
    if(!currentCase.isInstanceOf[RoadCase]) return;
    val currentRoad = currentCase.asInstanceOf[RoadCase]
    player.addScore(Items.GetValue(currentRoad.Item));
    if(currentRoad.Item == Items.PowerPellet) makeGhostsVulnarable()
    currentRoad.Item = Items.None;
  }

  private def makeGhostsVulnarable(): Unit = {
    ghosts.foreach(g => g.makeVulnerable())
  }
}
