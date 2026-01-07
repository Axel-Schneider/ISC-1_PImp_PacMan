package Backend

import Frontend.GameRenderer
import Backend.global.Levels // Si tu as ce fichier, sinon on définit une map ici

object PacmanApp {
  def main(args: Array[String]): Unit = {

    // 1. Définition du niveau (Map)
    // Légende: w=mur, r=route, P=joueur, G=fantôme, d=petit point, D=gros point, v=porte
    // 2. Initialisation de la Logique
    val gameLogic = new Logical()

    try {
      gameLogic.LoadLevel(Levels.Level1)
    } catch {
      case e: Exception =>
        println("Erreur chargement niveau: " + e.getMessage)
        sys.exit(1)
    }

    // 3. Initialisation du Rendu Graphique
    val renderer = new GameRenderer(gameLogic)

    // 4. ABONNEMENT : On dit à la logique "Appelle renderer.render(this) à chaque frame"
    gameLogic.subscribeCycle(renderer.render)

    // 5. Démarrage
    println("Démarrage du jeu...")
    gameLogic.startGame()

    // Note importante :
    // Ta classe Logical utilise 'TimeUnit.SECONDS' dans le mainLoopThreadExecutor.
    // Cela signifie que le jeu va se mettre à jour 1 fois par seconde.
    // C'est très lent pour un jeu fluide.
    // Tu devras probablement changer 'TimeUnit.SECONDS' en 'TimeUnit.MILLISECONDS'
    // et ajuster le délai (ex: 100ms) dans Logical.scala si le jeu saccade.
  }
}
