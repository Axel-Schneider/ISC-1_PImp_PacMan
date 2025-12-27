package Frontend

import Backend.Logical

class MainView(private val Logical: Logical) {
  // TO DO : User interface of the game
  print(Logical.Map.map(l => l.mkString).mkString("\n"))
}
