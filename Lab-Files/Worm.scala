package blockbattle

 class Worm (var pos: Pos){
    import Game._
    import BlockWindow._
    import Main._

  def nextRandomPos(): Pos = {
    import scala.util.Random.nextInt
    val x = nextInt(Game.windowSize._1)
    val y = nextInt(Game.windowSize._2 - 7) + 7
    Pos(x, y)
  }

  
  def isHere(p: Pos): Boolean = pos == p
  
  def draw(window: BlockWindow): Unit = window.setBlock(pos, Game.Color.worm)
  
  def erase(window: BlockWindow): Unit = window.setBlock(pos, Game.Color.soil)
  
  val teleportProbability = 0.02
  
  def randomTeleport(notHere1: Pos, notHere2: Pos, window: BlockWindow): Unit =
    if (math.random() < teleportProbability) {
      erase(window)
      do pos = nextRandomPos() while (pos == notHere1 || pos == notHere2)
      draw(window)
    }
}


