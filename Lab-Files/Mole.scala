package blockbattle

case class Mole(
    val name: String,
    var pos: Pos,
    var dir: (Int, Int),
    val color: java.awt.Color,
    val keyControl: KeyControl
){
    var points = 0

    override def toString = s"Mole[name=$name, pos=$pos, dir=$dir, point=$points]"

    /** Om keyControl.has(key) så uppdateras riktningen dir enligt keyControl */
    def setDir (key: String): Unit = {
        if(keyControl.has(key)) this.dir = keyControl.direction(key)
    }

    /** Uppdaterar dir till motsatta riktningen */
    def reverseDir(): Unit = this.dir = (-this.dir._1, -this.dir._2)

    /** Uppdaterar pos så att den blir nextPos */
    def move(): Unit = {
        pos = nextPos
    }

    /** Ger nästa position enligt riktningen dir utan att uppdatera pos*/
    def nextPos: Pos = {
        pos.moved(dir)
    }
}

object Mole {
    val startPos: Pos = Pos(0,0)
}