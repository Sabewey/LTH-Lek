package nlp

case class Text(source: String){
  lazy val words: Vector[String] = {
    val onlyLetters = (source.map(char => if(char.isLetter) char else ' '))
    val x: Array[String] = onlyLetters.split(' ')
    val y = x.filter(s => s.length > 0)
    val w = y.map(s => s.toLowerCase())
    w.toVector
  }  // dela upp source i ord

  lazy val distinct: Vector[String] = words.distinct

  lazy val wordSet: Set[String] = words.toSet

  lazy val wordsOfLength: Map[Int, Set[String]] = wordSet.groupBy(_.length)

  lazy val wordFreq: Map[String, Int] = {
    val fmb = new FreqMapBuilder
    val xs = Text(source).words
    xs.foreach(s => fmb.add(s))
    fmb.toMap
  }  // använd FreqMapBuilder

  def ngrams(n: Int): Vector[Vector[String]] = {
    val xs = Text(source).words
    xs.sliding(n).toVector
  } // använd sliding

  lazy val bigrams: Vector[(String, String)] =
    ngrams(2).map(xs => (xs(0), xs(1)))

  lazy val followFreq: Map[String, Map[String, Int]] = { 
    val result = scala.collection.mutable.Map.empty[String, FreqMapBuilder]
   for ((key, next) <- Text(source).bigrams) {
     if (result.contains(key)) { 
     result(key).add(next) 
     } else {
       result += (key -> new FreqMapBuilder)
       result(key).add(next)
     }
   }
   result.map(p => p._1 -> p._2.toMap).toMap
  } //nästlad tabell

  lazy val follows: Map[String, String] =
    followFreq.map { case (key, followMap) => 
      val maxByFreq: (String, Int) = followMap.maxBy(_._2)
      val mostCommonFollower: String = maxByFreq._1
      (key, mostCommonFollower) 
    }
    //eller kortare med samma resultat: (lättare eller svårare att läsa?)
    //  followFreq.map(kv => kv._1 -> kv._2.maxBy(_._2)._1)
}

object Text {
  def fromFile(fileName: String, encoding: String = "UTF-8"): Text = {
    val source = scala.io.Source.fromFile(fileName, encoding).mkString
    Text(source)
  }
  def fromURL(url: String, encoding: String = "UTF-8"): Text = {
    val source = scala.io.Source.fromURL(url, encoding).mkString
    Text(source)
  }
}
