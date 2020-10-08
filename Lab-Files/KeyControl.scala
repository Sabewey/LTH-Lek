package blockbattle

case class KeyControl(left: String, right: String, up: String, down: String) {
    def direction(key: String): (Int, Int) = {
        if(this.left == key)       (-1, 0)
        else if(this.right == key) (1,  0)
        else if(this.up == key)    (0,  -1)
        else if(this.down == key)  (0,  1)
        else (0, 4)
    }
    
    def has(key: String): Boolean = {
        if (this.left == key) true
        else if (this.right == key) true
        else if (this.up == key) true
        else if (this.down == key) true
        else false
    }
}