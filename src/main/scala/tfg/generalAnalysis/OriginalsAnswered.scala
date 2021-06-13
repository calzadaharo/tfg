package tfg.generalAnalysis

import com.raphtory.api.Analyser

import scala.collection.mutable.ArrayBuffer

object OriginalsAnswered {
  def apply():OriginalsAnswered = new OriginalsAnswered(Array())
}

class OriginalsAnswered(args:Array[String]) extends Analyser(args) {
  object sortOrdering extends Ordering[Int] {
    def compare(key1: Int, key2: Int) = key2.compareTo(key1)
  }
  override def analyse(): Unit = {}

  override def setup(): Unit = {}

  def returnResults(): Any = {
    val vertices = view.getVertices().map { vertex =>
      val degree = vertex.getIncEdges.size
      val original = vertex.getPropertyValue("original").getOrElse("")
      (vertex.ID, degree, original)
    }

    val originals = vertices.filter(x => x._3 == "true").size
    val noAnswer = vertices.filter(x => x._3 == "true").filter(x => x._2 == 0).size
    val noOriginals = vertices.map(x => x._2).sum

    return (originals, noAnswer, noOriginals)
  }

  override def defineMaxSteps(): Int = 1

  def processResults(results: ArrayBuffer[Any], timeStamp: Long, viewCompleteTime: Long): Unit = {
    val endResults  = results.asInstanceOf[ArrayBuffer[(Int, Int, Int)]]

    var my_output_file = System.getenv().
      getOrDefault("OUTPUT_FILE", "OriginalsAnswered.json").trim

    val originals = endResults.map(x => x._1).sum
    val noAnswered = endResults.map(z => z._2).sum
    val noOriginals = endResults.map(z => z._3).sum

    val percentage =
      try ((noAnswered.toDouble / originals.toDouble)*100)
      catch {
        case e: ArithmeticException => 0
      }

    val percentageBis =
      try (100-percentage)
      catch {
        case e:ArithmeticException => 0
      }

    val text =
      s"""{"time":$timeStamp, "originals":$originals, "no-answered":$noAnswered, "percentage-isolated":$percentage,
         |"percentage-answered":$percentageBis},""".stripMargin

    writeLines(my_output_file, text, "{\"views\":[")
    println(text)
  }
}