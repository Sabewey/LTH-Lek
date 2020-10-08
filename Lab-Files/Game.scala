package blockbattle
import java.awt.{Color => JColor}
import BlockWindow._
import Game._ // direkt tillgång till namn på medlemmar i kompanjon
import Mole._
//import Worm._


object Game {
  val windowSize = (30, 50)
  val windowTitle = "EPIC BLOCK BATTLE"
  val blockSize = 14
  val skyRange = 0 to 7
  val grassRange = 8 to 8
  val startDir: (Int, Int) = (0, 1)
  val kc1: KeyControl = KeyControl(left = "a", right = "d", up = "w", down = "s")
  val kc2: KeyControl = KeyControl(left = "j", right = "l", up = "i", down = "k")
  var quit = false
  val delayMillis = 80

  //Text Constants
  val centerX: Int = windowSize._1/2
  val offSetX: Int = 10
  val textPos1: Pos = Pos(centerX - offSetX, 0)
  val textPos2: Pos = Pos(centerX, 0)
  
  object Color {
    val black = new JColor( 0, 0, 0) 
    val mole = new JColor( 51, 51, 0)
    val soil = new JColor(153, 102, 51)
    val tunnel = new JColor(204, 153, 102)
    val grass = new JColor( 25, 130, 35)
    val sky = new JColor(135, 206, 235)
    val worm = new JColor(225, 100, 235)
}

  /** Used with the different ranges and eraseBlocks */
  def backgroundColorAtDepth(y: Int): java.awt.Color = ???
}


case class Game(
    val leftPlayerName: String = "LEFT",
    val rightPlayerName: String = "RIGHT"
) {


  val window = new BlockWindow(windowSize, windowTitle, blockSize)
  val leftMole: Mole = Mole(leftPlayerName, Pos((windowSize._1 / 2), (windowSize._2 / 2)), startDir, Color.mole, kc1)
  val rightMole: Mole = Mole(rightPlayerName, Pos((windowSize._1 / 2 + 2), (windowSize._2/2 + 2)), startDir, Color.mole, kc2)

  var wormPos = Pos(10, 10)
  val worm1: Worm = new Worm(wormPos)
  

  def drawWorld(): Unit = {
    window.rectangle(Pos(0,0), size = (windowSize._1, skyRange.last), Color.sky)
    window.rectangle(Pos(0, skyRange.last), size = (windowSize._1, grassRange.length), Color.grass)
    window.rectangle(Pos(0, grassRange.last), size = (windowSize._1, windowSize._2 - grassRange.last), Color.soil)
  }

  /** Use to erase old points, e.g updated score */
  def eraseBlocks(x1: Int, y1: Int, x2: Int, y2: Int): Unit = ???

  
  
  def countPoints(mole: Mole): String = {
    var nextBlock = Color.black
    if(mole.nextPos.y < windowSize._2 && mole.nextPos.x < windowSize._1 && mole.nextPos.x > 0) nextBlock = window.getBlock(mole.nextPos)
    if (nextBlock == Color.soil) mole.points += 1
    mole.points.toString()
  }


  
  
  def showPoints(window: BlockWindow, mole: Mole):Unit = {
    if(mole.name == leftPlayerName) window.write(text = s"$leftPlayerName  pts: " + countPoints(mole), textPos1, Color.black)
    if(mole.name == rightPlayerName) window.write(text = s"$rightPlayerName  pts: " + countPoints(mole), textPos2, Color.black)
  }

  
  
  
  def update(mole: Mole, window: BlockWindow): Unit = {
    showPoints(window, mole)
    if(mole.nextPos.x >= windowSize._1 || mole.nextPos.x < 0) mole.reverseDir()
    if(mole.nextPos.y < skyRange.last || mole.nextPos.y >= windowSize._2) mole.reverseDir()
    window.setBlock(mole.nextPos, mole.color)
    window.setBlock(mole.pos, Color.tunnel)

    mole.move()
  } // update, draw new, erase old

  
  
  
  def gameOver(mole: Mole, window: BlockWindow): Unit = {
    if (mole.points > 1000) {
      quit = true  
      window.write("GAME OVER", Pos(centerX - offSetX, offSetX), Color.black, 40)
    }
  }



  def updateWorm(worm: Worm, window: BlockWindow, leftMole: Mole, rightMole: Mole): Unit = {
    if(worm.isHere(leftMole.pos)) {
      worm.pos = worm.nextRandomPos()
      worm.erase(window)
      
      leftMole.points += 500
    }

    if(worm.isHere(rightMole.pos)) { 
      worm.pos = worm.nextRandomPos()
      worm.erase(window)

      rightMole.points += 500
    } 

    worm.draw(window)
    worm.randomTeleport(leftMole.pos, rightMole.pos, window)
  }




  def updateAll(): Unit = {
    window.rectangle(Pos(0,0), size = (windowSize._1, skyRange.last - 1), Color.sky)
    update(leftMole, window)
    update(rightMole, window)
    gameOver(leftMole, window)
    gameOver(rightMole, window)
    updateWorm(worm1, window, leftMole, rightMole)
  }




  def handleEvents(): Unit = {
    var e = window.nextEvent()
    while(e != BlockWindow.Event.Undefined) {
      e match {
        case BlockWindow.Event.KeyPressed(key) => key match {
          case "a" => leftMole.setDir(key)
          case "d" => leftMole.setDir(key)
          case "w" => leftMole.setDir(key)
          case "s" => leftMole.setDir(key)
          case "j" => rightMole.setDir(key)
          case "i" => rightMole.setDir(key)
          case "l" => rightMole.setDir(key)
          case "k" => rightMole.setDir(key)
          case "q" => quit = true
          case _ => ""
        }

        case BlockWindow.Event.WindowClosed => println("Window Closing"); quit = true
      }
      e = window.nextEvent()
    }
  }





  def gameLoop(): Unit = {
    while(!quit) {
      val t0 = System.currentTimeMillis()
      handleEvents()
      updateAll()

      val elapsedMillis = (System.currentTimeMillis - t0).toInt
      Thread.sleep((delayMillis - elapsedMillis) max 0)
    }
  }



  def start(): Unit = {
    println("Start digging!")
    println(s"$leftPlayerName ${leftMole.keyControl}")
    println(s"$rightPlayerName ${rightMole.keyControl}")
    drawWorld()
    gameLoop()
  }
}
