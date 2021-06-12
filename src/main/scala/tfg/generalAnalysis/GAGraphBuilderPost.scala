package tfg.generalAnalysis

import com.raphtory.core.actors.Router.GraphBuilder
import com.raphtory.core.model.communication._
import sun.security.ec.point.ProjectivePoint.Immutable

class GAGraphBuilderPost extends GraphBuilder[String]{

  override def parseTuple(tuple: String) = {

    val fileLine = tuple.split(";").map(_.trim)
    //print(fileLine)

    val post = fileLine(1)
    val postID = assignID(post)

    val postAuthor = fileLine(2)

    val parentPost = fileLine(4)
    val parentPostID = assignID(parentPost)

//    val parentPostAuthor = fileLine(5)

    val timeProcess = new TimeProcessing()

    val timeStamp: Long = timeProcess.time(fileLine(0));

    if (parentPost.toInt > -1) {
      addVertex(timeStamp, postID,
        Properties(ImmutableProperty("name",post),
          // No es necesario en el análisis general
          ImmutableProperty("author",postAuthor),
          ImmutableProperty("original","false")),
        Type("Post"))
//      addVertex(timeStamp, parentPostID,
//        Properties(
//          ImmutableProperty("name",post)),
//          // No es necesario en el análisis general
//          // ImmutableProperty("author",parentPostAuthor)),
//          // MutableProperty("original",null)),
//        Type("Post"))
      addEdge(timeStamp, postID, parentPostID, Type("Post answer"))
    }

    else{
      //es un mensaje original: crear nodo, con propiedad original a True, y usuario el creador del post

      addVertex(timeStamp, postID,
        Properties(ImmutableProperty("name", post),
          ImmutableProperty("author", postAuthor),
          ImmutableProperty("original", "true")),
        Type("Post"))
    }
  }
}