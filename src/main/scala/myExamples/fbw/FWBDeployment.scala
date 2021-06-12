package myExamples.fbw

import com.raphtory.RaphtoryGraph
import com.raphtory.algorithms.{ConnectedComponents, DegreeBasic}

object FWBDeployment extends App {
  //print("HHHHHHHHHHOOOOOOOOOOOOLLLLLLLLLLLAAAAAAAAAAA")
  val source  = new FBWSpout()
  val builder = new FWBGraphBuilder()
  val rg = RaphtoryGraph[String](source,builder)
  val arguments = Array[String]()

  //rg.rangeQuery(SixDegreesOfGandalf(3),start = 1,end = 32674,increment = 100,arguments)

  //rg.rangeQuery(ConnectedComponents(),start = 1095135831,end = 1128389387,increment = 1000000,arguments)
  //rg.rangeQuery(ConnectedComponents(),start = 1,end = 32674,increment = 100,window=100,arguments)
  //rg.rangeQuery(ConnectedComponents(),start = 1,end = 32674,increment = 100,windowBatch=Array(100,50,10),arguments)

  rg.viewQuery(DegreeBasic(),timestamp = 1232598691,arguments)
  //rg.viewQuery(DegreeBasic(),timestamp = 10000,window=100,arguments)
  //rg.viewQuery(DegreeBasic(),timestamp = 10000,windowBatch=Array(100,50,10),arguments)
}
