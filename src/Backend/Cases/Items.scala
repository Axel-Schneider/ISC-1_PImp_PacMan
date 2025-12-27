package Backend.Cases

object Items extends Enumeration {
  type Items = Value;
  val None, PacDot, PowerPellet, Cherry, Strawberry, Orange, Apple, Melon, Galaxian, Bell, Key = Value;

  def GetValue(item: Items): Int = {
    item match {
      case None => 0;
      case PacDot => 10;
      case PowerPellet => 50;
      case Cherry => 100;
      case Strawberry => 300;
      case Orange => 500;
      case Apple => 700;
      case Melon => 1000;
      case Galaxian => 2000;
      case Bell => 3000;
      case Key => 5000;
      case _ => 0;
    }
  }
}