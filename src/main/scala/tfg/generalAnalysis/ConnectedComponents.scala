package tfg.generalAnalysis

import com.raphtory.api.Analyser

import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.immutable

object ConnectedComponents {
  def apply():ConnectedComponents = new ConnectedComponents(Array())
}

class ConnectedComponents(args:Array[String]) extends Analyser(args){

  override def setup(): Unit =
    view.getVertices().foreach { vertex =>
      vertex.setState("cclabel", vertex.ID)
      vertex.messageAllNeighbours(vertex.ID)
    }

  override def analyse(): Unit =
    view.getMessagedVertices().foreach { vertex =>
      val label  = vertex.messageQueue[Long].min
      if (label < vertex.getState[Long]("cclabel")) {
        vertex.setState("cclabel", label)
        vertex messageAllNeighbours label
      }
      else
        vertex.voteToHalt()
    }


  override def returnResults(): Any =
    view.getVertices()
      .map(vertex => vertex.getState[Long]("cclabel"))
      .groupBy(f => f)
      .map(f => (f._1, f._2.size))

  override def processResults(results: ArrayBuffer[Any], timestamp: Long, viewCompleteTime: Long): Unit = {
    val er = extractData(results)
    val text = s"""{"time":$timestamp,"top5":${er.top5},"totalIslands":${er.total},"totalIsolatedIslands":${er.totalIslands},"proportion":${er.proportion},"clustersGT2":${er.totalGT2},"viewTime":$viewCompleteTime},"""
    var output_file = System.getenv().getOrDefault("OUTPUT_FILE","ConnectedComponents.json").trim

    println(text)
    writeLines(output_file, text, "{\"views\":[")

    publishData(text)
  }

  override def processWindowResults(results: ArrayBuffer[Any], timestamp: Long, windowSize: Long, viewCompleteTime: Long): Unit = {
    val er = extractData(results)
    var output_file = System.getenv().getOrDefault("OUTPUT_FILE","ConnectedComponents.json").trim
    val text = s"""{"time":$timestamp,"windowsize":$windowSize,"top5":[${er.top5.mkString(",")}],"total":${er.total},"totalIslands":${er.totalIslands},"proportion":${er.proportion},"clustersGT2":${er.totalGT2},"viewTime":$viewCompleteTime},"""
    writeLines(output_file, text, "[")
    println(text)
    publishData(text)

  }

  def extractData(results:ArrayBuffer[Any]):extractedData ={
    val endResults = results.asInstanceOf[ArrayBuffer[immutable.ParHashMap[Long, Int]]]
    try {
      val grouped = endResults.flatten.groupBy(f => f._1).mapValues(x => x.map(_._2).sum)
      val groupedNonIslands = grouped.filter(x => x._2 > 1)
      val biggest = grouped.maxBy(_._2)._2
      val sorted = groupedNonIslands.groupBy(_._2).map(f => (f._1, f._2.size)).toArray.sortBy(_._1)(sortOrdering)
      val top5 = sorted.map(x => s"""{"size":${x._1},"freq":${x._2}}""")
        .mkString("[",",","]")
      val total = grouped.size
      val totalWithoutIslands = groupedNonIslands.size
      val totalIslands = total - totalWithoutIslands
      val proportion = biggest.toFloat / grouped.map(x => x._2).sum
      val totalGT2 = grouped.filter(x => x._2 > 2).size
      extractedData(top5,total,totalIslands,proportion,totalGT2)
    } catch {
      case e: UnsupportedOperationException => extractedData("",0,0,0,0)
    }
  }

  override def defineMaxSteps(): Int = 1000

}
object sortOrdering extends Ordering[Int] {
  def compare(key1: Int, key2: Int) = key2.compareTo(key1)
}
case class extractedData(top5:String,total:Int,totalIslands:Int,proportion:Float,totalGT2:Int)
