package tfg.generalAnalysis

import com.raphtory.RaphtoryGraph
import com.raphtory.algorithms.{DegreeBasic}

object GADeployment extends App {
  val source  = new GASpout()
  val builder = new GAGraphBuilderPost()
  val rg = RaphtoryGraph[String](source,builder)
  val arguments = Array[String]()
  val timeProcess = new TimeProcessing()

  val total_end = timeProcess.time("2016-10-10T16:37:48+00:00")
  val relation_end = timeProcess.time("2016-10-10T16:37:36+00:00")
  val total_init = timeProcess.time("2016-08-10T00:00:00+00:00")

  val hour: Int = 3600
  val day: Int = 3600*24
  val week: Int = 3600*24*7
  val month: Int = 3600*24*30

  val mini_init = timeProcess.time("2016-08-10T03:58:37+00:00")
  val mini_end = timeProcess.time("2016-08-10T21:19:19+00:00")

  /**********DEGREE ORIGINALS**********/
//  rg.viewQuery(DegreeOriginals(),timestamp=mini_end,arguments)
//  rg.viewQuery(DegreeOriginals(),timestamp=total_end,arguments)
//  rg.rangeQuery(DegreeOriginals(),start=mini_init+day,end=mini_end,increment=day,window=day,arguments)
//  rg.rangeQuery(DegreeOriginals(),start=total_init+day,end=total_end,increment=day,window=day,arguments)

  /**********ORIGINALS ANSWERED**********/
//  rg.viewQuery(OriginalsAnswered(),timestamp=mini_end,arguments)
//  rg.viewQuery(OriginalsAnswered(),timestamp=total_end,arguments)

  /**********ANSWERS AVERAGE**********/
//  rg.viewQuery(AnswersAverage(),timestamp=mini_end,arguments)
//  rg.viewQuery(AnswersAverage(),timestamp=total_end,arguments)

  /**********DISTRIBUTIONS**********/
//  rg.viewQuery(Distributions(),timestamp=mini_end,arguments)
//  rg.viewQuery(Distributions(),timestamp=total_end,arguments)

  /**********CONNECTED COMPONENTS**********/
//  rg.viewQuery(ConnectedComponents(),timestamp=mini_end,arguments)
//  rg.viewQuery(ConnectedComponents(),timestamp=total_end,arguments)

  /**********DEPTH DISTRIBUTION**********/
//    rg.viewQuery(DepthDistribution(),timestamp=mini_end,arguments)
//    rg.viewQuery(DepthDistribution(),timestamp=total_end,arguments)

  /**********DEPTH TREE**********/
//  rg.viewQuery(DepthTree(),timestamp=mini_end,arguments)
//  rg.viewQuery(DepthTree(),timestamp=total_end,arguments)

  /**********AUTHORS TREE**********/
//  rg.viewQuery(AuthorsTree(),timestamp=mini_end,arguments)
//  rg.viewQuery(AuthorsTree(),timestamp=total_end,arguments)

}