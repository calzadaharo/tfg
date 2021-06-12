package tfg.generalAnalysis

import com.raphtory.api.Analyser

import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.immutable
import scala.collection.mutable.ListBuffer

object AuthorsTree {
  def apply():AuthorsTree = new AuthorsTree(Array())
}

class AuthorsTree(args:Array[String]) extends Analyser(args) {

  override def setup(): Unit = {
    view.getVertices().foreach { vertex =>
        vertex.setState("label",vertex.ID)
        vertex.messageAllNeighbours(vertex.ID)
    }
  }

  override def analyse(): Unit = {
    view.getMessagedVertices().foreach { vertex =>
      val label = vertex.messageQueue[Long].min
      if (label < vertex.getState[Long]("label")) {
        vertex.setState("label", label)
        vertex.messageAllNeighbours(label)
      }
      else {
        vertex.voteToHalt()
      }
    }
  }

  override def returnResults(): Any = {
    view.getVertices()
      .map(x => (x.getState("label"), x.getPropertyValue("author").getOrElse("")))
  }

  override def defineMaxSteps(): Int = 1000

  override def processResults(results: ArrayBuffer[Any], timeStamp: Long, viewCompleteTime: Long): Unit = {
    var output_file = System.getenv().getOrDefault("OUTPUT_FILE","AuthorsTree.json").trim

    val endResults = results.asInstanceOf[ArrayBuffer[immutable.ParHashMap[Long, String]]].flatten

    val grouped = endResults
      .groupBy(x=>x._1)
      .map(x=>x._2)
      .filter(x=>x.size != 1)

    val definitive = grouped.map(x=> {
      val authors = new ListBuffer[String]
      x.foreach(y => authors+=y._2)
      (x.size,authors.groupBy(x=>x).size)
    }).map(x => s"""{"size":${x._1},"authors":${x._2}}""")
      .mkString("[",",","]")

    val text =
      s"""{"time":$timeStamp,"total":$definitive,"count":${grouped.size}}
         |]}""".stripMargin

    writeLines(output_file, text, "{\"views\":[")
    print(text)
  }
}