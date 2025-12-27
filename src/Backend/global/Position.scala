package Backend.global

class Position {
  protected var x: Int = 0;
  protected var y: Int = 0;

  protected def definePosition(x: Int, y: Int): Unit = {
    this.x = x;
    this.y = y;
  }

  def X: Int = x
  def Y: Int = y;
}
