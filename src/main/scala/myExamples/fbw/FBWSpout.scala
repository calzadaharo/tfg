package myExamples.fbw

import com.raphtory.core.actors.Spout.Spout

import scala.collection.mutable

class FBWSpout extends Spout[String] {
  //print("AAAAADDDDDDIIIIIIOOOOOOSSSSSS")
  val fileQueue = mutable.Queue[String]()

  override def setupDataSource(): Unit = {
    fileQueue++=
      scala.io.Source.fromFile("src/main/scala/myExamples/fbw/out.facebook-wosn-wall")
        .getLines
  }//no setup

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