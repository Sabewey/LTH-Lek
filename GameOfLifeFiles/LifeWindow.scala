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
    for (j <- 1 to rows) window.line(blockSize * j, 0, blockSize * j, blockSize * cols, java.awt.Color.DARK_GRAY)
  }

  def drawCell(row: Int, col: Int): Unit = {
    val color = if (life.cells(row, col)) new java.awt.Color(242, 128, 161) else java.awt.Color.black
    window.fill(row * blockSize + 1, col * blockSize + 1, blockSize - 1, blockSize - 1, color)
  }

  def update(newLife: Life): Unit = {
    val oldLife = life
    life = newLife
    life.cells.foreachIndex{ drawCell }
  }

  def handleKey(key: String): Unit = key match {
    //case "ENTER" => update(Life(rows, cols))
    case "r"         => update(Life.random(rows, cols))
    //buggig
    case "t"         => update(life.evolved())
    case "BACKSPACE" => {println("backspace was pressed"); update(Life.empty(rows, cols))}
    case _       => ""
  }

  def handleClick(pos: (Int, Int)): Unit = ???

  def loopUntilQuit(): Unit = while (!quit) {
    val t0 = System.currentTimeMillis
    if (play) update(life.evolved())
    window.awaitEvent(EventMaxWait)
    while (window.lastEventType != PixelWindow.Event.Undefined) {
      window.lastEventType match {
        case Event.KeyPressed  =>  handleKey(window.lastKey)
        case Event.MousePressed => handleClick(window.lastMousePos)
        case Event.WindowClosed => quit = true
        case _ =>
      }
      window.awaitEvent(EventMaxWait)
    }
    val elapsed = System.currentTimeMillis - t0
    Thread.sleep((NextGenerationDelay - elapsed) max 0)
  }

  def start(): Unit = { drawGrid(); update(Life.random(rows, cols));loopUntilQuit() }
}
