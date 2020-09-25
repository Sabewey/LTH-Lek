package blockmole
import java.awt.{Color => JColor}


object Color {
    val black = new JColor( 0, 0, 0) 
    val mole = new JColor( 51, 51, 0)
    val soil = new JColor(153, 102, 51)
    val tunnel = new JColor(204, 153, 102)
    val grass = new JColor( 25, 130, 35)
}

object BlockWindow {
    import introprog.PixelWindow

    val windowSize = (30, 50)
    val blockSize = 10

    val window = new PixelWindow(windowSize._1 * blockSize, windowSize._2 * blockSize, "BlockMole Game")

    type Pos = (Int, Int)

    def block(pos: Pos)(color: JColor = JColor.gray): Unit = {
        val x = pos._1 * blockSize
        val y = pos._2 * blockSize
        window.fill(x, y, blockSize, blockSize, color)
    }

    def rectangle(leftTop: Pos)(size: (Int, Int))(color: JColor = JColor.gray) {
        for (y <- leftTop._2 to size._2) {
            println("r")
            for (x <- leftTop._1 to size._1) {
                block(x, y)(color)
                println("Nu ritas")
            }
        }
    }

    val maxWaitMillis = 10
    def waitForKey(): String = {
        window.awaitEvent(maxWaitMillis)
        while( window.lastEventType != PixelWindow.Event.KeyPressed) {
            window.awaitEvent(maxWaitMillis) //skip other events
        }
        println(s"KeyPressed: " + window.lastKey)
        window.lastKey
    }
}

object Mole {
    def dig(): Unit = {
        var x = BlockWindow.windowSize._1 / 2
        var y = BlockWindow.windowSize._2 / 2
        var quit = false

        while (!quit) {
            BlockWindow.block(x, y)(Color.mole)
            val key = BlockWindow.waitForKey()
            if      (key == "w") y -= 1
            else if (key == "a") x -= 1
            else if (key == "s") y += 1
            else if (key == "d") x += 1
            else if (key == "q") quit = true
        }
    }
}


object Main {
    def drawWorld(): Unit = {
        println("Ska rita underjorden!")
        BlockWindow.rectangle(0, 0)(size = (30, 4))(Color.grass)
        BlockWindow.rectangle(0, 4)(size = (30, 46))(Color.soil)

    } 

    def main(args: Array[String]): Unit = {
        drawWorld()
        Mole.dig()
    }
}