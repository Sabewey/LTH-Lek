package snake
import introprog.BlockGame

abstract class SnakeGame(title: String) extends BlockGame(
  title, dim = (50, 30), blockSize = 15, background = Colors.Background,
  framesPerSecond = 50, messageAreaHeight = 3
) {
  var entities: Vector[Entity] = Vector.empty

  var players: Vector[Player] = Vector.empty

  sealed trait State
  case object Starting extends State
  case object Playing  extends State
  case object GameOver extends State
  case object Quitting extends State

  var state: State = Starting

  def enterStartingState(): Unit = { 
    clearWindow()
    drawCenteredText("tryck space för start")
    state = Starting
  } // rensa, meddela "tryck space för start"

  def enterPlayingState(): Unit = {
    clearWindow()
    entities.foreach(_.draw)
    state = Playing
  } // rensa, rita alla entiteter

  def enterGameOverState(): Unit = { 
    drawCenteredText("game over")
    state = GameOver 
  } // meddela "game over"

  def enterQuittingState(): Unit = {
    println("Goodbye!")
    pixelWindow.hide()
    state = Quitting
  }

  def randomFreePos(): Pos = {
    var found = false
    var tryPos = Pos.random(Dim(dim))
    while (!found) {
      if (entities.forall(p => !p.isOccupyingBlockAt(tryPos))) found = true
      tryPos = Pos.random(Dim(dim))
    }
    tryPos
  } // dra slump-pos tills ledig plats, används av frukt

  override def onKeyDown(key: String): Unit = {
    println(s"""key "$key" pressed""")
    state match {
      case Starting => if (key == " ") enterPlayingState()
      case Playing => players.foreach(_.handleKey(key))
      case GameOver =>
        if (key == " ") enterPlayingState()
        else if(key == "Escape") enterQuittingState()
      case _ =>
    }
  }

  override def onClose(): Unit = enterQuittingState()

  def isGameOver(): Boolean  // abstrakt metod, implementeras i subklass

  override def gameLoopAction(): Unit = {
    if (state == Playing) {
      entities.foreach(_.erase)
      entities.foreach(_.update)
      entities.foreach(_.draw)
      if (isGameOver()) enterGameOverState()
    }
  }

  def startGameLoop(): Unit = {
    pixelWindow.show()  // möjliggör omstart även om fönstret stängts...
    enterStartingState()
    gameLoop(stopWhen = state == Quitting)
  }

  def play(playerNames: String*): Unit // abstrakt, implementeras i subklass
}
