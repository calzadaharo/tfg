package tfg.generalAnalysis

import com.raphtory.api.Analyser

import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.immutable

object DepthDistribution {
  def apply():DepthDistribution = new DepthDistribution(Array())
}

class DepthDistribution(args:Array[String]) extends Analyser(args) {

  override def setup(): Unit = {
    view.getVertices().foreach { vertex =>
      if (vertex.getPropertyValue("original").getOrElse("") == "true") {
        vertex.setState("SEP", 0)
        vertex.messageAllNeighbours(0)
      } else {
        vertex.setState("SEP", -1)
      }
    }
  }

  override def analyse(): Unit = {
    view.getMessagedVertices().foreach { vertex =>
      if (vertex.getState[Int]("SEP") == -1) {
        var label = vertex.messageQueue[Int].min
        label += 1
        vertex.setState("SEP", label)
        vertex.messageAllNeighbours(label)
      }
      else {
        vertex.voteToHalt()
      }
    }
  }

  override def returnResults(): Any = {
    view.getVertices().map{vertex =>
      if(vertex.getState[Int]("SEP")==(-1)){
        print(vertex.getPropertyValue("name")+"     ")
      }
      (vertex.getState[Int]("SEP"))}
      .groupBy(f => f)
      .map(f => (f._1, f._2.size))
  }

  override def defineMaxSteps(): Int = 1000

  def processResults(results: ArrayBuffer[Any], timeStamp: Long, viewCompleteTime: Long): Unit = { // Muestra los id de los -1
    val endResults = results.asInstanceOf[ArrayBuffer[immutable.ParHashMap[Int, Int]]]

    var my_output_file = System.getenv().
      getOrDefault("OUTPUT_FILE", "MaxThread.json").trim

    val threads =  endResults.flatten.groupBy(f => f._1).mapValues(x => x.map(_._2).sum) //
      .toArray
      .sortBy(x => x._1)
      .map(x => s"""{"thread":${x._1},"freq":${x._2}}""")
      .mkString("[",",","]")

    val text =
      s"""{"time":$timeStamp, "threads":$threads}""".stripMargin

//    writeLines(my_output_file, text, "{\"views\":[")
    println(text)
  }
}



