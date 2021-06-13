package tfg.generalAnalysis

import scala.collection.mutable.ArrayBuffer
import com.raphtory.api.Analyser

object AnswersAverage {
  def apply() = new AnswersAverage(Array())
}

class AnswersAverage(args:Array[String]) extends Analyser(args) {

  object sortOrdering extends Ordering[Int] {
    def compare(key1: Int, key2: Int) = key2.compareTo(key1)
  }

  override def analyse(): Unit = {}

  override def setup(): Unit = {}

  override def returnResults(): Any = {
    val vertices = view.getVertices().map { vertex =>
      val originals = vertex.getPropertyValue("original").getOrElse("")
      val degree = vertex.getIncEdges.size
      (vertex.ID,originals,degree)
    }

    val answers = vertices.map(x => x._3).sum
    val originals = vertices.filter(x => x._2 == "true").filter(x => x._3 != 0).size
    return (answers,originals)
  }

  override def defineMaxSteps(): Int = 1

  def processResults(results: ArrayBuffer[Any], timeStamp: Long, viewCompleteTime: Long): Unit = {
    val endResults  = results.asInstanceOf[ArrayBuffer[(Int, Int)]]

    var my_output_file = System.getenv().
      getOrDefault("OUTPUT_FILE", "AnswersAverage.json").trim

    val answers = endResults.map(x => x._1).sum
    val originals = endResults.map(z => z._2).sum

    val average =
      try answers.toDouble / originals.toDouble
      catch {
        case e: ArithmeticException => 0
      }

    val text =
      s"""{"time":$timeStamp, "originals":$originals, "answers":$answers, "average-thread": $average},""".stripMargin

    writeLines(my_output_file, text, "{\"views\":[")
    println(text)
  }
}

