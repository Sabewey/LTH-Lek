object PongGame {
  import scala.io.StdIn.readLine
  import scala.util.Random
  val random = new Random

  def chooseName(): String = readLine("Vad heter spelaren?  ")
  def welcomeMsg(name: String): Unit = println(s"""
    Welcome to the Pong World $name
    In this arena only the best of the best pong players survive...
    Will you beat the champion and survive or loose and face the consequences?
    """)

  def playerChoice(variety: Vector[String]): String = {
    var msg = variety(random.nextInt(variety.length))

    println(s"""
        $msg

        Which side do you return the ball on?
        |LEFT| or |RIGHT| ? 
        """)

    readLine()

  }


  //Game loop
  def play(): Unit = {
    val os: Vector[String] = Vector(
      "The computer hit the ball back at a whopping 100 km/h!!!",
      "With a quick flick of the wrist the computer sliced the ball back at you",
      "With a 360 spin the computer noscopes the ball",
      "The computer blocked",
      "The computer SMASHES",
      "Woah, the computer hit a great shot",
      "Fast shot from computer"
    )

    val name = chooseName()
    welcomeMsg(name)

    var gameIsOver = true
    var i = 0
    while (i < 5 && gameIsOver) {
      var Choice = playerChoice(os)
      var ballPath: String = if (math.random < 0.2) "RIGHT" else "LEFT"                                          //            80 % chance to hit if LEFT, 20 % to hit if RIGHT

      if (Choice == ballPath) {
        println("You manage to return the ball BUT...")
        i = i + 1
      } else gameIsOver = false

    }

    if (gameIsOver) {
      println(s"Congratulations $name you won!")
    } else println("You missed and the pong ball hit you so hard you died...")
  }

  //Main function: Starts the program
  def main(args: Array[String]): Unit = {
    play()
  }
}
