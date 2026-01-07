package Frontend

import Backend.Logical
import Frontend.Sprite.SpriteManager
import hevs.graphics.FunGraphics

class MainView(private val Logical: Logical) {
  // TO DO : User interface of the game
  Logical.subscribeCycle(refreshUserInterface)
  SpriteManager.loadSprites()
  val renderer = new Renderer(Logical.Map.length * SpriteManager.SPRITE_SIZE, Logical.Map(0).length * SpriteManager.SPRITE_SIZE)

  renderer.drawSprite(SpriteManager.getSprite("python"), Logical.Map.length-1, Logical.Map(0).length-1)

  def refreshUserInterface(logical: Logical): Unit = {
    print(logical.Map.map(l => l.mkString).mkString("\n"))
  }
}
