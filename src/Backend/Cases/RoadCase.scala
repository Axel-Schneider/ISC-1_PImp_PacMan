package Backend.Cases

import Backend.Cases.Items.Items
import Backend.Entities.Ghosts.Ghosts
import Backend.Entities.{Entity, Player}

class RoadCase extends Case(CaseType.Road) {
  var Item: Items = Items.None;
  var Entities: Array[Entity] = Array.empty;

  override def toString: String = {
    if(!Entities.isEmpty)
      if(Entities.exists(e => e.isInstanceOf[Player])) "o";
      else if (Entities.exists(e => e.isInstanceOf[Ghosts])) "U"
      else "?"
    else Item match {
      case Items.PacDot => ".";
      case Items.PowerPellet => "•";
      // TO DO : Implement fruits
      case _ => " ";
    }
  };
}