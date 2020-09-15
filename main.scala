object PongGame {
    import scala.io.StdIn.readLine
    
    var score: Int = 0

    def welcomeMsg(): Unit = println( s$"Welcome to the Pong World " + 
    """In this arena only the best of the best pong players survive...
    Will you beat the champion and survive or loose and face the consequences?
    """)

    def chooseName(): String = readLine("Vad heter spelaren?")
    def play(): Unit = {
        chooseName()
    }

    def main(args: Array[String]): Unit = {
        chooseName()
        welcomeMsg()
    }
}
