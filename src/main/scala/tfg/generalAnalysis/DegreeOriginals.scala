package tfg.generalAnalysis

import com.raphtory.api.Analyser
import scala.collection.mutable.ArrayBuffer

object DegreeOriginals {
  def apply() = new DegreeOriginals(Array())
}

class DegreeOriginals(args:Array[String]) extends Analyser(args) {
  object sortOrdering extends Ordering[Int] {
    def compare(key1: Int, key2: Int) = key2.compareTo(key1)
  }
  override def analyse(): Unit = {}

  override def setup(): Unit = {}

  def returnResults(): Any = {
    val vertices = view.getVertices().map{vertex =>
      //Cambiar si no se emieza desde el inicio de GAB
      val degree = vertex.getIncEdges.size
      val original = vertex.getPropertyValue("original").getOrElse("")
      (vertex.ID, degree, original)
    }

    val noOriginals = vertices.filter(x => x._3 == "false").size
    val originals = vertices.filter(x => x._3 == "true").size
    val noClassified = vertices.filter(x => x._3 == "")
//      .map{vertex =>
//        (vertex.ID) //Long
//      }
    return (noOriginals,originals,noClassified)
  }

  override def defineMaxSteps(): Int = 1

  def processResults(results: ArrayBuffer[Any], timeStamp: Long, viewCompleteTime: Long): Unit = {
    val endResults  = results.asInstanceOf[ArrayBuffer[(Int, Int, Int)]]

    var my_output_file = System.getenv().
      getOrDefault("OUTPUT_FILE", "DegreeOriginals.json").trim

    val noOriginals = endResults.map(x => x._1).sum
    val originals = endResults.map(z => z._2).sum
    val noClassi = endResults.map(z => z._3).sum

    val degree =
      try noOriginals.toDouble / originals.toDouble
      catch {
        case e: ArithmeticException => 0
      }

    val oriXans = 1/degree

    val perOriginal = 100*((originals.toDouble)/(originals.toDouble+noOriginals.toDouble))

    val perAnswer = 100*((noOriginals.toDouble)/(originals.toDouble+noOriginals.toDouble))

    val text =
      s"""{"time":$timeStamp, "originals":$originals, "answers":$noOriginals, "don't-know": $noClassi, "relation": $degree,
         | "original-per-answer": $oriXans, "percentage-originals": $perOriginal, "percentage-answers":$perAnswer},""".stripMargin

    writeLines(my_output_file, text, "{\"views\":[")
    println(text)
  }
}