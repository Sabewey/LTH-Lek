package blockbattle
import java.awt.{Color => JColor}
import java.nio.file.attribute.PosixFileAttributes



class BlockWindow(
    val nbrOfBlocks: (Int, Int),
    val title: String = "BLOCK WINDOW",
    val blockSize: Int = 14
) {
  import introprog.PixelWindow
  val pixelWindow = new PixelWindow(
    nbrOfBlocks._1 * blockSize,
    nbrOfBlocks._2 * blockSize,
    title
  )

  def setBlock(pos: Pos, color: java.awt.Color): Unit = {
    val bx = pos.x * blockSize
    val by = pos.y * blockSize
    pixelWindow.fill(bx, by, blockSize, blockSize, color)
  }

  def rectangle(leftTop: Pos, size: (Int, Int), color: JColor = JColor.gray) {
    for (y <- leftTop.y to size._2 + leftTop.y) {
      for(x <- leftTop.x to size._1 + leftTop.x){
        setBlock(Pos(x, y), color)
      }
    }
  }

  def getBlock(pos: Pos): java.awt.Color =  pixelWindow.getPixel(pos.x * blockSize, pos.y * blockSize)

  def write(
      text: String,
      pos: Pos,
      color: java.awt.Color,
      textSize: Int = blockSize
  ): Unit =
    pixelWindow.drawText(
      text,
      pos.x * blockSize,
      pos.y * blockSize,
      color,
      textSize
    )

  def nextEvent(maxWaitMillis: Int = 10): BlockWindow.Event.EventType = {
    import BlockWindow.Event._
    pixelWindow.awaitEvent(maxWaitMillis)
    pixelWindow.lastEventType match {
      case PixelWindow.Event.KeyPressed   => KeyPressed(pixelWindow.lastKey)
      case PixelWindow.Event.WindowClosed => WindowClosed
      case _                              => Undefined
    }
  }
}

object BlockWindow {
  import blockbattle._

  def delay(millis: Int): Unit = Thread.sleep(millis)
  
  object Event {
    trait EventType
    case class KeyPressed(key: String) extends EventType
    case object WindowClosed extends EventType
    case object Undefined extends EventType
  }
}
