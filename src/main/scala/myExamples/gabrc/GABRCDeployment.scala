package myExamples.gabrc

import com.raphtory.RaphtoryGraph
import com.raphtory.algorithms.{ConnectedComponents}

object GABRCDeployment extends App {
  //print("HHHHHHHHHHOOOOOOOOOOOOOOLLLLLLLLLLLAAAAAAAAAAA")
  val source  = new GABRCSpout()
  val builder = new GABRCGraphBuilderPost()
  val rg = RaphtoryGraph[String](source,builder)
  val arguments = Array[String]()
  val timeProcess = new TimeProcessing()
  val time = timeProcess.time("2016-10-10T16:37:48+00:00")
  val time_relation = timeProcess.time("2016-10-10T16:37:36+00:00")
  val inicio = timeProcess.time("2016-08-10T00:00:00+00:00")
  val day = 86400
  val hora: Int = 3600
  val dia: Int = 3600*24
  val semana: Int = 3600*24*7
  val mes: Int = 3600*24*30
  val inicio_mini = timeProcess.time("2016-08-10T13:25:26+00:00")
  val final_mini = timeProcess.time("2016-08-10T14:32:34+00:00")

  //rg.rangeQuery(SixDegreesOfGandalf(3),start = 1,end = 32674,increment = 100,arguments)

//  rg.rangeQuery(DegreeBasic(),start = inicio,end = inicio+day+day,increment = 3600,arguments)
//  rg.rangeQuery(DegreeRanking(),start = inicio,end = time_relation,increment = 3600,window=3600,arguments)
  //rg.rangeQuery(ConnectedComponents(),start = 1,end = 32674,increment = 100,windowBatch=Array(100,50,10),arguments)

//  rg.viewQuery(DegreeBasic(),timestamp = inicio + day,arguments)
//  rg.viewQuery(DegreeBasic(),timestamp = time_relation,window=day,arguments)
  //rg.viewQuery(DegreeBasic(),timestamp = 24424668,windowBatch=Array(100,50,10),arguments)

//  rg.viewQuery(ThreadCreators(),timestamp = final_mini,arguments)
//  rg.rangeQuery(ThreadAverage(),start = inicio,end = time_relation,increment=hora,window=hora,arguments)

//    rg.viewQuery(Distributions(),timestamp=time,arguments)
  rg.viewQuery(Distributions(),timestamp=time,arguments)


  //  for (i <- 1 to numero_dias) {
////    print(i + "  ")
//    rg.viewQuery(DegreeRanking(),timestamp = inicio+i*3600*24,window=3600*24,arguments)
//    Thread.sleep(20)
//  }

}