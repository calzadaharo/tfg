package tfg.generalAnalysis

import com.raphtory.api.Analyser

import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.immutable
import scala.collection.mutable.ListBuffer

object DepthTree {
  def apply():DepthTree = new DepthTree(Array())
}

class DepthTree(args:Array[String]) extends Analyser(args) {

  override def setup(): Unit = {
    view.getVertices().foreach { vertex =>
      if (vertex.getPropertyValue("original").getOrElse("") == "true") {
        vertex.setState("label",vertex.ID)
        vertex.setState("SEP",1)
        vertex.messageAllNeighbours(vertex.ID,1)
      } else {
        vertex.setState("label",vertex.ID)
        vertex.setState("SEP",0)
        vertex.messageAllNeighbours(vertex.ID,0)
      }
    }
  }

  override def analyse(): Unit = {
    view.getMessagedVertices().foreach { vertex =>
      val queue = vertex.messageQueue[(Long, Int)]
      var labels = new ListBuffer[Long]
      var seps = new ListBuffer[Int]
      queue.foreach(message => {
        labels += message._1
        seps += message._2
      })
      val label: Long = labels.min
      var sep: Int = seps.max
      var message = false
      if (label < vertex.getState[Long]("label")) {
        vertex.setState("label", label)
        message = true
      }
      if (sep != (0) && vertex.getState("SEP")==(0)) {
        sep += 1
        vertex.setState("SEP",sep)
        message = true
      }
      if(message == true){
        vertex.messageAllNeighbours(label,sep)
      } else {
        vertex.voteToHalt()
      }
    }
  }

  override def returnResults(): Any = {
    view.getVertices()
      .map(vertex => (vertex.getState[Long]("label"),vertex.getState[Int]("SEP")))
  }

  override def defineMaxSteps(): Int = 1000

  override def processResults(results: ArrayBuffer[Any], timestamp: Long, viewCompleteTime: Long): Unit = {
    var output_file = System.getenv().getOrDefault("OUTPUT_FILE","DepthTree.json").trim

    val endResults = results.asInstanceOf[ArrayBuffer[immutable.ParHashMap[Long, Int]]].flatten
    val grouped = endResults
      .groupBy(x => x._1)
      .map(x => x._2)
      .filter(x => x.size != 1)
      .map(x => {
        val z=x.map(y=>y._2)
        (z)
      })

    val present = grouped.map(x=> {
      var z = -1
      x.foreach(y =>
        if (y > z) {
          z = y
        }
      )
      (x.size, z)
    }).map(x => s"""{"size":${x._1},"max thread":${x._2}}""")
      .mkString("[",",","]")

    //    print("           "+ grouped.map(x=>x.size) + "            ")

    val text = s"""{"time":$timestamp,"probando":$present,"OTRA COSA":${grouped.size}}"""
//    ,"OTRA COSA":${grouped.size}
    writeLines(output_file, text, "{\"views\":[")
    print(text)
  }

//  def processData(data:ArrayBuffer[(Long,Int)])= {
//    return data.groupBy(x => x._1)
//  }

}