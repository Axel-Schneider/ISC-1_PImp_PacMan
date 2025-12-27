import Backend.Logical
import Frontend.MainView

class PacMan {
  val backend = new Logical();
  val frontend = new MainView(backend);
}
