package Frontend

import Frontend.Sprite.Sprite
import hevs.graphics.FunGraphics

class Renderer(w: Int, h: Int) {
  val display = new FunGraphics(w, h)

  def drawSprite(sprite: Sprite, x: Int, y: Int): Unit = {
    if (x < 0 || x >= display.width / sprite.size) return
    if (y < 0 || y >= display.height / sprite.size) return

    for (i <- 0 until sprite.size) {
      for (j <- 0 until sprite.size) {
        display.setPixel(x*sprite.size + i, y*sprite.size + j, sprite.pixels(i)(j))
      }
    }
  }
}
