package Frontend

import hevs.graphics.FunGraphics
import java.awt.Color
import java.awt.event.{KeyAdapter, KeyEvent}
import Backend.Logical
import Backend.Cases._
import Backend.Entities.Directions
import Backend.Cases.Items

class GameRenderer(logical: Logical) {
  val CELL_SIZE = 30
  val mapHeight = logical.Map.length
  val mapWidth = logical.Map(0).length
  val colorBlue = new Color(33, 33, 222)
  val display = new FunGraphics(mapWidth * CELL_SIZE, mapHeight * CELL_SIZE + 40, "Pac-Man Scala")

  display.setKeyManager(new KeyAdapter {
    override def keyPressed(e: KeyEvent): Unit = {
      val newDir = e.getKeyCode match {
        case KeyEvent.VK_UP    => Some(Directions.Up)
        case KeyEvent.VK_DOWN  => Some(Directions.Down)
        case KeyEvent.VK_LEFT  => Some(Directions.Left)
        case KeyEvent.VK_RIGHT => Some(Directions.Right)
        case _ => None
      }

      if(newDir.isDefined) {
        logical.ChangePlayerDirection(newDir.get)
      }
    }
  })

  def render(game: Logical): Unit = {
    display.frontBuffer.synchronized {
      display.setColor(Color.BLACK)
      display.drawFillRect(0, 0, display.getFrameWidth, display.getFrameHeight)
      for (y <- 0 until mapHeight) {
        for (x <- 0 until mapWidth) {
          val currentCase = game.Map(y)(x)
          drawCase(currentCase, x, y)
        }
      }
      for (ghost <- game.Ghosts) {
        var ghostColor = ghost.MainColor
        if (ghost.IsVulnerable) {
          ghostColor = Color.BLUE
          if (ghost.IsBlinking) {
            ghostColor = Color.WHITE
          }
        }
        drawEntity(ghost.X, ghost.Y, ghostColor, isRound = false)
      }
      if (game.Player.IsAlive) {
        drawEntity(game.Player.X, game.Player.Y, Color.YELLOW, isRound = true)
      }
      if (!game.IsGamePlaying) {
        display.setColor(Color.RED)
        display.drawString(10, mapHeight * CELL_SIZE + 20, "PAUSE / GAME OVER")
      }
    }
  }

  private def drawCase(c: Case, x: Int, y: Int): Unit = {
    val px = x * CELL_SIZE
    val py = y * CELL_SIZE
    c.CaseType match {
      case CaseType.Wall =>
        display.setColor(colorBlue)
        display.drawFillRect(px, py, CELL_SIZE, CELL_SIZE)
        display.setColor(Color.BLACK)
        display.drawRect(px + 4, py + 4, CELL_SIZE - 8, CELL_SIZE - 8)

      case CaseType.Door =>
        display.setColor(Color.PINK)
        display.drawFillRect(px, py + (CELL_SIZE / 2) - 2, CELL_SIZE, 4)

      case CaseType.Road =>
        val road = c.asInstanceOf[RoadCase]
        road.Item match {
          case Items.PacDot =>
            display.setColor(new Color(255, 184, 174))
            display.drawFillRect(px + CELL_SIZE/2 - 2, py + CELL_SIZE/2 - 2, 4, 4)
          case Items.PowerPellet =>
            display.setColor(new Color(255, 184, 174))
            display.drawFilledOval(px + CELL_SIZE/2 - 6, py + CELL_SIZE/2 - 6, 12, 12)
          case _ =>
        }
      case CaseType.Empty =>
    }
  }

  private def drawEntity(x: Int, y: Int, color: Color, isRound: Boolean): Unit = {
    val px = x * CELL_SIZE
    val py = y * CELL_SIZE

    display.setColor(color)
    if (isRound) {
      display.drawFilledOval(px + 2, py + 2, CELL_SIZE - 4, CELL_SIZE - 4)
    } else {
      display.drawFilledOval(px + 4, py + 4, CELL_SIZE - 8, CELL_SIZE - 8)
      display.drawFillRect(px + 4, py + (CELL_SIZE/2), CELL_SIZE - 8, (CELL_SIZE/2) - 2)
    }
  }
}