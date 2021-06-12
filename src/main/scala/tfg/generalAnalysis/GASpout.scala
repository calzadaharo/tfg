package tfg.generalAnalysis

import com.raphtory.core.actors.Spout.Spout

import scala.collection.mutable

class GASpout extends Spout[String] {
  val fileQueue = mutable.Queue[String]()

  override def setupDataSource(): Unit = {
        fileQueue++=
          scala.io.Source.fromFile("src/main/scala/tfg/datasets/miniGab.csv")
            .getLines
//        fileQueue++=
//          scala.io.Source.fromFile("src/main/scala/tfg/datasets/gabNetwork500.csv")
//            .getLines
  }

  override def generateData(): Option[String] = {
    if(fileQueue isEmpty){
      dataSourceComplete()
      None
    }
    else
      Some(fileQueue.dequeue())
  }
  override def closeDataSource(): Unit = {}//no file closure already done
}