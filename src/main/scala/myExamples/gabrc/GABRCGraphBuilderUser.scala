package myExamples.gabrc

import com.raphtory.core.actors.Router.GraphBuilder
import com.raphtory.core.model.communication._

class GABRCGraphBuilderUser extends GraphBuilder[String]{

  override def parseTuple(tuple: String) = {

    val fileLine = tuple.split(";").map(_.trim)
    //print(fileLine)

    val user = fileLine(2)
    val userID = assignID(user)

    val parentUser = fileLine(5)
    val parentUserID = assignID(parentUser)

    var mensajes_originales = 0

    val timeProcess = new TimeProcessing

    val timeStamp: Long = timeProcess.time(fileLine(0));
    //print("Funciona esto?:   " + timeStamp + "           ?          ")

    /*addVertex(timeStamp, postID, Properties(ImmutableProperty("name",postNode)),Type("Character"))
    addVertex(timeStamp, userID, Properties(ImmutableProperty("name",userNode)),Type("Character"))
    addVertex(timeStamp, parentPostID, Properties(ImmutableProperty("name",parentPostNode)),Type("Character"))
    addVertex(timeStamp, parentUserID, Properties(ImmutableProperty("name",userNode)),Type("Character"))
    addEdge(timeStamp,userID,parentUserID, Type("Character Co-occurence"))*/

    if (parentUser.toInt > 0) {
//      sendUpdate(VertexAddWithProperties(timeStamp, userID,
//        Properties(ImmutableProperty("name", user)), Type("Character")))
      addVertex(timeStamp, userID,
        Properties(ImmutableProperty("name",user)),Type("Character"))
      //    sendUpdate(VertexAddWithProperties(timeStamp, postID,          ¡No funciona!
      //      Properties(ImmutableProperty("name",post)),Type("Character")))
//      sendUpdate(VertexAddWithProperties(timeStamp, parentUserID,
//        Properties(ImmutableProperty("name", parentUser)), Type("Character")))
      addVertex(timeStamp, parentUserID,
        Properties(ImmutableProperty("name",parentUser)),Type("Character"))
      //    sendUpdate(VertexAddWithProperties(timeStamp, parentPostID,
      //      Properties(ImmutableProperty("name",parentPost)),Type("Character")))

//      sendUpdate(EdgeAdd(timeStamp, userID, parentUserID, Type("Character Co-occurence")))
      addEdge(timeStamp,userID,parentUserID, Type("Character Co-occurence"))

    }

    else {
      mensajes_originales += 1;
    }
  }
}