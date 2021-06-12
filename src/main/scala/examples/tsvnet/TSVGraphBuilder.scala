package com.raphtory.examples.tsvnet

import com.raphtory.core.actors.Router.GraphBuilder
import com.raphtory.core.model.communication.{EdgeAdd, Type, VertexAdd}

/** Spout for network datasets of the form SRC_NODE_ID DEST_NODE_ID TIMESTAMP */
class TSVGraphBuilder extends GraphBuilder[String] {
  override def parseTuple(tuple: String) = {
    val fileLine = tuple.split(" ").map(_.trim)
    //user wise
    val sourceNode = fileLine(0).toInt
    val targetNode = fileLine(1).toInt
    val creationDate = fileLine(2).toLong



    //comment wise
    // val sourceNode=fileLine(1).toInt
    //val targetNode=fileLine(4).toInt
    if (targetNode > 0) {
      val creationDate = fileLine(2).toLong
      addVertex(creationDate, sourceNode, Type("User"))
      addVertex(creationDate, targetNode, Type("User"))
      addEdge(creationDate, sourceNode, targetNode, Type("User to User"))

    }
  }
}
