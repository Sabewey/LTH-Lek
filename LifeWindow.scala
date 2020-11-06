package life

import introprog.PixelWindow
import introprog.PixelWindow.Event

object LifeWindow {
  val EventMaxWait = 1 // milliseconds
  var NextGenerationDelay = 200 // milliseconds
  val title: String = "GAME OF LIFE"
  val blockSize = 14
  // lägg till fler användbara konstanter här tex färger etc.
}

class LifeWindow(rows: Int, cols: Int){
  import LifeWindow._ // importera namn från kompanjon

  var life = Life.empty(rows, cols)
  val window: PixelWindow = new PixelWindow(rows * blockSize, cols * blockSize, title)
  var quit = false
  var play = false

  def drawGrid(): Unit = {
    for (i <- 1 to cols) window.line(0, blockSize * i, rows * blockSize, i * blockSize, java.awt.Color.DARK_GRAY)
    for (j <- 1 to rows) window.line(blockSize * j, 0, blockSize * j, blockSize * cols, java.awt.Color.BLUE)
  }

  def drawCell(row: Int, col: Int): Unit = {
    val color = if (life.cells(row, col)) {
      new java.awt.Color(242, 128, 161)
    } else if (life.nbrOfNeighbours(row, col) == 3){
      new java.awt.Color(40, 0, 0)
    } else {
      java.awt.Color.black
    }
    window.fill(row * blockSize + 1, col * blockSize + 1, blockSize - 1, blockSize - 1, color)
  }

  def update(newLife: Life): Unit = {
    val oldLife = life
    life = newLife
    life.cells.foreachIndex{ drawCell }
  }

  def handleKey(key: String): Unit = key match {
    case "r"         => update(Life.random(rows, cols))
    case "Enter"     => update(life.evolved())
    case "Backspace" => update(Life.empty(rows, cols))
    case " "         => play = !play
    case _           => ""
  }

  def handleClick(pos: (Int, Int)): Unit = {update(life.toggled(pos._1 / blockSize, pos._2 / blockSize)); println("Cellen du skapade har " + life.nbrOfNeighbours(pos._1 / blockSize, pos._2 / blockSize) + "st grannar")}

  def loopUntilQuit(): Unit = while (!quit) {
    val t0 = System.currentTimeMillis
    if (play) update(life.evolved())
    window.awaitEvent(EventMaxWait)
    while (window.lastEventType != PixelWindow.Event.Undefined) {
      window.lastEventType match {
        case Event.KeyPressed  =>  handleKey(window.lastKey); println(s"du tröck: " + window.lastKey)
        case Event.MousePressed => handleClick(window.lastMousePos); println(s"du tröck: " + window.lastMousePos._1 / blockSize + " " + window.lastMousePos._2 / blockSize)
        case Event.WindowClosed => quit = true
        case _ =>
      }
      window.awaitEvent(EventMaxWait)
    }
    val elapsed = System.currentTimeMillis - t0
    Thread.sleep((NextGenerationDelay - elapsed) max 0)
  }

  def start(): Unit = { drawGrid(); println(Life.random(rows, cols)(-1, -1)); update(Life.random(rows, cols)); loopUntilQuit() }
}

//; println(Life.random(rows, cols).nbrOfNeighbours(0, 0))
// println(Life.random(rows, cols).nbrOfNeighbours(30, 39))