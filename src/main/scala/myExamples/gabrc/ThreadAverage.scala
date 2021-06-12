package myExamples.gabrc

import com.raphtory.api.Analyser
import scala.collection.mutable.ArrayBuffer

object ThreadAverage {
  def apply() = new ThreadAverage(Array())
}

class ThreadAverage(args:Array[String]) extends Analyser(args){
  object sortOrdering extends Ordering[Int] {
    def compare(key1: Int, key2: Int) = key2.compareTo(key1)
  }

  override def analyse(): Unit = {}
  override def setup(): Unit   = {}
  override def returnResults(): Any = {
    val degree =
        view.getVertices().map { vertex =>
          val outDegree = vertex.getOutEdges.size
          val inDegree = vertex.getIncEdges.size
          (vertex.ID, outDegree, inDegree)
        }

    val original = degree.filter(post => post._2 == 0).size
    val son = degree.filter(post => post._2 > 0).size
    val topUsers = degree.toArray.sortBy(x => x._3)(sortOrdering).take(20)
    (original, topUsers, son, topUsers)
  }

  override def defineMaxSteps(): Int = 1

  override def processResults(results: ArrayBuffer[Any], timeStamp: Long, viewCompleteTime: Long): Unit = {
    val endResults  = results.asInstanceOf[ArrayBuffer[(Int, Int, Int, Array[(Int, Int, Int)])]]
    //    var output_file = System.getenv().getOrDefault("GAB_PROJECT_OUTPUT", "/app/defout.csv").trim
    var my_output_file = System.getenv().getOrDefault("OUTPUT_FILE", "ThreadAverage.json").trim
    val startTime   = System.currentTimeMillis()
    val totalOriginals   = endResults.map(x => x._1).sum
    val totalSons   = endResults.map(x => x._3).sum
    val degree =
      try totalSons.toDouble / totalOriginals.toDouble
      catch { case e: ArithmeticException => 0 }
    var bestUserArray = "["
    val bestUsers = endResults
      .map(x => x._4)
      .flatten
      .sortBy(x => x._3)(sortOrdering)
      .take(20)
      .map(x => s"""{"id":${x._1},"indegree":${x._3},"outdegree":${x._2}}""")
      .foreach(x => bestUserArray += x + ",")
    bestUserArray = if (bestUserArray.length > 1) bestUserArray.dropRight(1) + "]" else bestUserArray + "]"
    val text =
      s"""{"time":$timeStamp,"originals":$totalOriginals,"sons":$totalSons,"average thread":$degree,"bestusers":$bestUserArray,"viewTime":$viewCompleteTime,"concatTime":${System
        .currentTimeMillis() - startTime}},"""
    writeLines(my_output_file, text, "{\"views\":[")
    println(text)
    publishData(text)
  }

  override def processWindowResults(
                                     results: ArrayBuffer[Any],
                                     timestamp: Long,
                                     windowSize: Long,
                                     viewCompleteTime: Long
                                   ): Unit = {
    val endResults  = results.asInstanceOf[ArrayBuffer[(Int, Int, Int, Array[(Int, Int, Int)])]]
    var output_folder = System.getenv().getOrDefault("OUTPUT_FOLDER", "/app").trim
    var output_file = output_folder + "/" + System.getenv().getOrDefault("OUTPUT_FILE","ThreadAverage.json").trim
    var my_output_file = System.getenv().getOrDefault("OUTPUT_FILE", "DegreeRanking.csv").trim
    val startTime   = System.currentTimeMillis()
    val totalOriginals   = endResults.map(x => x._1).sum
    val totalSons   = endResults.map(x => x._3).sum
    val degree =
      try totalSons.toDouble / totalOriginals.toDouble
      catch { case e: ArithmeticException => 0 }
    var bestUserArray = "["
    val bestUsers = endResults
      .map(x => x._4)
      .flatten
      .sortBy(x => x._3)(sortOrdering)
      .take(20)
      .map(x => s"""{"id":${x._1},"indegree":${x._3},"outdegree":${x._2}}""")
      .foreach(x => bestUserArray += x + ",")
    bestUserArray = if (bestUserArray.length > 1) bestUserArray.dropRight(1) + "]" else bestUserArray + "]"
    val text =
      s"""{"time":$timestamp,"windowsize":$windowSize,"originals":$totalOriginals,"sons":$totalSons,"average thread":$degree,"bestusers":$bestUserArray,"viewTime":$viewCompleteTime,"concatTime":${System
        .currentTimeMillis() - startTime}},"""
    writeLines(my_output_file, text, "{\"views\":[")
    println(text)
    //publishData(text)
  }
}