package Frontend

import hevs.graphics.FunGraphics
import java.awt.Color
import java.awt.event.{KeyAdapter, KeyEvent}
import Backend.Logical
import Backend.Entities.Directions

class GameRenderer(logical: Logical) {
  val CELL_SIZE = 30
  val mapHeight = logical.Map.length
  val mapWidth = logical.Map(0).length

  // Fenêtre un peu plus haute pour afficher le score
  val display = new FunGraphics(mapWidth * CELL_SIZE, mapHeight * CELL_SIZE + 50, "Pac-Man Graphique")

  // --- Gestion Clavier (Identique) ---
  display.setKeyManager(new KeyAdapter {
    override def keyPressed(e: KeyEvent): Unit = {
      val newDir = e.getKeyCode match {
        case KeyEvent.VK_UP    => Some(Directions.Up)
        case KeyEvent.VK_DOWN  => Some(Directions.Down)
        case KeyEvent.VK_LEFT  => Some(Directions.Left)
        case KeyEvent.VK_RIGHT => Some(Directions.Right)
        case _ => None
      }
      if(newDir.isDefined) logical.ChangePlayerDirection(newDir.get)
    }
  })

  // --- Rendu basé sur les caractères (toString) ---
  def render(game: Logical): Unit = {
    display.frontBuffer.synchronized {
      // Fond noir
      display.setColor(Color.BLACK)
      display.drawFillRect(0, 0, display.getFrameWidth, display.getFrameHeight)

      for (y <- 0 until mapHeight) {
        for (x <- 0 until mapWidth) {
          val currentCase = game.Map(y)(x)
          // C'est ici que la magie opère : on récupère le caractère (ex: "#", "o", ".")
          val symbol = currentCase.toString.trim

          drawSymbol(symbol, x, y)
        }
      }

      // Afficher le score (si tu as une variable score dans logical.Player)
      display.setColor(Color.WHITE)
      display.drawString(10, mapHeight * CELL_SIZE + 30, s"Score: ${game.Player.Score}") // Suppose que Player a un champ Score
    }
  }

  def drawSymbol(symbol: String, x: Int, y: Int): Unit = {
    val px = x * CELL_SIZE
    val py = y * CELL_SIZE
    val centerX = px + CELL_SIZE / 2
    val centerY = py + CELL_SIZE / 2

    symbol match {
      case "#" => // MUR
        display.setColor(Color.BLUE)
        display.drawFillRect(px, py, CELL_SIZE, CELL_SIZE)
        display.setColor(Color.BLACK)
        display.drawRect(px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10)

      case "." => // PETIT POINT (PacDot)
        display.setColor(new Color(255, 184, 174)) // Rose pâle
        display.drawFillRect(centerX - 2, centerY - 2, 4, 4)

      case "•" => // GROS POINT (PowerPellet) - Copie ce symbole depuis ta console
        display.setColor(new Color(255, 184, 174))
        display.drawFilledOval(centerX - 8, centerY - 8, 16, 16)

      case "o" => // PACMAN
        display.setColor(Color.YELLOW)
        display.drawFilledOval(px + 2, py + 2, CELL_SIZE - 4, CELL_SIZE - 4)
      // (Optionnel) Tu pourrais dessiner une bouche ici selon la direction

      case "U" => // FANTÔME (Normal)
        display.setColor(Color.RED) // Par défaut Rouge car toString ne donne pas la couleur
        drawGhostShape(px, py, Color.RED)

      case "X" => // FANTÔME (Vulnérable)
        drawGhostShape(px, py, Color.BLUE)

      case "Y" => // FANTÔME (Clignotant)
        drawGhostShape(px, py, Color.WHITE)

      case "G" => // SPAWN FANTÔME (Si visible)
        // Rien ou un petit carré debug
        display.setColor(Color.DARK_GRAY)
        display.drawRect(px, py, CELL_SIZE, CELL_SIZE)

      case _ =>
      // Espace vide ou inconnu : on ne dessine rien (noir)
    }
  }

  // Petite fonction utilitaire pour dessiner un fantôme
  def drawGhostShape(px: Int, py: Int, c: Color): Unit = {
    display.setColor(c)
    display.drawFilledOval(px + 4, py + 4, CELL_SIZE - 8, CELL_SIZE - 8)
    display.drawFillRect(px + 4, py + CELL_SIZE/2, CELL_SIZE - 8, CELL_SIZE/2 - 4)
  }
}