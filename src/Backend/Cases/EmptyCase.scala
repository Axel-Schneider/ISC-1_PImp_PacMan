package Backend.Cases

class EmptyCase(posX: Int, posY: Int) extends Case(CaseType.Empty, posX, posY) {
  override def toString: String = " ";
}
