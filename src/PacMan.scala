import Backend.Logical
import Backend.global.Levels
import Frontend.{GameRenderer, MainView}

class PacMan {
  val backend = new Logical();
  backend.LoadLevel(Levels.Level1)
  val renderer = new GameRenderer(backend)
  backend.subscribeCycle(renderer.render)
  Thread.sleep(5000)
  backend.startGame()
}
