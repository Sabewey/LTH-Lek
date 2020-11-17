package nlp

class FreqMapBuilder {
    private val register = collection.mutable.Map.empty[String, Int]
    def toMap: Map[String, Int] = register.toMap
    def add(s: String): Unit = {
        register += (s -> (register.getOrElse(s, 0) + 1))
    }
}
object FreqMapBuilder {
    def apply(xs: String*): FreqMapBuilder = {
        val result = new FreqMapBuilder
        xs.foreach(result.add)
        result
    } 
}

