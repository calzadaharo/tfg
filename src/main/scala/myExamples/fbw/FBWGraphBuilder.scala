package myExamples.fbw

import com.raphtory.core.actors.Router.GraphBuilder
import com.raphtory.core.model.communication._

class FWBGraphBuilder extends GraphBuilder[String]{

  override def parseTuple(tuple: String) = {

    val fileLine = tuple.split(" ").map(_.trim)
    //print(fileLine)

    val sourceNode = fileLine(0)
    val srcID = assignID(sourceNode)


    val targetNode = fileLine(1)
    val tarID = assignID(targetNode)


    val timeStamp = fileLine(3).toLong


//    sendUpdate(VertexAddWithProperties(timeStamp, srcID,
//      Properties(ImmutableProperty("name",sourceNode)),Type("Character")))
    addVertex(timeStamp, srcID,
      Properties(ImmutableProperty("name",sourceNode)),Type("Character"))
//    sendUpdate(VertexAddWithProperties(timeStamp, tarID,
//      Properties(ImmutableProperty("name",targetNode)),Type("Character")))
    addVertex(timeStamp, tarID,
      Properties(ImmutableProperty("name",targetNode)),Type("Character"))


//    sendUpdate(EdgeAdd(timeStamp,srcID,tarID, Type("Character Co-occurence")))
    addEdge(timeStamp,srcID,tarID, Type("Character Co-occurence"))
  }
}