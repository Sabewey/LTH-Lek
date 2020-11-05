package life

case class Life(cells: Matrix[Boolean]) {

  /** Ger true om cellen på plats (row, col) är vid liv annars false.
    * Ger false om indexeringen är utanför universums gränser.
    */
  def apply(row: Int, col: Int): Boolean = if (row <= cells.dim._1 - 1 && row > 0 && col <= cells.dim._2 - 1 && col > 0) cells(row, col) else false

  /** Sätter status på cellen på plats (row, col) till value. */
  def updated(row: Int, col: Int, value: Boolean): Life = Life(cells.updated(row, col)(value))

  /** Växlar status på cellen på plats (row, col). */
  def toggled(row: Int, col: Int): Life = Life(cells.updated(row, col)(!cells(row, col)))

  /** Räknar antalet levande grannar till cellen i (row, col). */
  def nbrOfNeighbours(row: Int, col: Int): Int = {
    var sum: Int = 0
    for (i <- row - 1 to row + 1){
      for (j <- col - 1 to col + 1) {
        if(cells(i, j)) sum +=1 
      }
    }
    //Eftersom att den kommer räkna sig själv
    sum - 1
  }

  /** Skapar en ny Life-instans med nästa generation av universum.
    * Detta sker genom att applicera funktionen \code{rule} på cellerna.
    */
  def evolved(rule: (Int, Int, Life) => Boolean = Life.defaultRule):Life = {
    var nextGeneration = Life.empty(cells.dim)
    cells.foreachIndex { (r,c) =>
      nextGeneration = nextGeneration.updated(r,c, Life.defaultRule(r,c, current = this))
    }
    nextGeneration
  }

  /** Radseparerad text där 0 är levande cell och - är död cell. */
  override def toString = ???
}

object Life {
  /** Skapar ett universum med döda celler. */
  def empty(dim: (Int, Int)): Life = Life(Matrix.fill(dim)(false))

  /** Skapar ett unviversum med slumpmässigt liv. */
  def random(dim: (Int, Int)): Life = {
    var random = empty(dim)
    for (i <- 0 to dim._1 - 1) {
      for (j <- 0 to dim._2 - 1) {
        random = random.updated(i, j, Math.random() > 0.5)
      }
    }
    random
  }

  /** Implementerar reglerna enligt Conways Game of Life. */
  def defaultRule(row: Int, col: Int, current: Life): Boolean = {
    //Om den lever...
    if (current.cells(row, col)) {
      if(current.nbrOfNeighbours(row, col) < 2 || current.nbrOfNeighbours(row, col) > 3) false else true
    }
    //Om den är död...
    if (!current.cells(row, col) && current.nbrOfNeighbours(row, col) == 3) true else false 
  }
}
