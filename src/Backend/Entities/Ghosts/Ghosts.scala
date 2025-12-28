package Backend.Entities.Ghosts

import Backend.Cases.Case
import Backend.Entities.Entity

import java.awt.Color
import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

abstract class Ghosts(val MainColor: Color) extends Entity {
  protected var isVulnerable: Boolean = false;
  protected var isBlinking: Boolean = false;
  protected var isAlive: Boolean = false;

  def IsVulnerable = isVulnerable;
  def IsBlinking = isBlinking;
  def IsAlive = isAlive;

  def kill: Unit = { isAlive = false; }
  def revive: Unit = { isAlive = true; }

  private val ex: ScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
  private val makeBlinkTask = new Runnable {
    override def run(): Unit = {
      if(IsVulnerable && !IsBlinking) {
          isBlinking = true
          return;
      }
      isVulnerable = false;
      isBlinking = false;
      if(!ex.isShutdown) ex.shutdown();
      println("makeBlinkTaskEnd")
    }
  }

  def makeVulnerable(): Unit = {
    // TO DO : Dynamic calcul timing of vulnerability
    isVulnerable = true;
    ex.scheduleAtFixedRate(makeBlinkTask, 5, 5, TimeUnit.SECONDS)
  }

  def resetVulnerability(): Unit = {
    isVulnerable = false;
    isBlinking = false;
  }

  def takeDecision(map: Array[Array[Case]]): Unit;
}