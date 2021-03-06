package actors

import scala.concurrent.duration._
import com.snappy.app.Receptionist
import akka.actor.Actor
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.api.libs.json.Json._
import akka.actor.Props
import akka.event.Logging


class TimerActor extends Actor {
  import com.snappy.app.Node.Put

  // crate a scheduler to send a message to this actor every socket

  case class UserChannel(userId: Int, var channelsCount: Int, enumerator: Enumerator[JsValue], channel: Channel[JsValue])
  
  // this map relate every user with his UserChannel
  var webSockets = Map[Int, UserChannel]()

  // this map relate every user with his current time
  var usersTimes = Map[Int, Int]()
  
  val receptionist = context.actorOf(Props(new Receptionist()), "receptionist")

  override def receive = {

    case StartSocket(userId) =>

      // get or create the touple (Enumerator[JsValue], Channel[JsValue]) for current user
      // Channel is very useful class, it allows to write data inside its related 
      // enumerator, that allow to create WebSocket or Streams around that enumerator and
      // write data iside that using its related Channel
      val userChannel: UserChannel = webSockets.get(userId) getOrElse {
        val broadcast: (Enumerator[JsValue], Channel[JsValue]) = Concurrent.broadcast[JsValue]
        UserChannel(userId, 0, broadcast._1, broadcast._2)
      }

      // if user open more then one connection, increment just a counter instead of create
      // another touple (Enumerator, Channel), and return current enumerator,
      // in that way when we write in the channel,
      // all opened WebSocket of that user receive the same data
      userChannel.channelsCount = userChannel.channelsCount + 1
      webSockets += (userId -> userChannel)

      // return the enumerator related to the user channel,
      // this will be used for create the WebSocket
      sender ! userChannel.enumerator

    case UpdateTime() =>

      // increase the current time for every user,
      // and send current time to the user,
      usersTimes.foreach {
        case (userId, millis) =>
          usersTimes += (userId -> (millis + 1000))
      }

    case NodeData(node: String, log: String) =>
      usersTimes.foreach {
        case (userId, millis) =>
          val json = Map("node" -> node, "data" -> log)
          webSockets.get(userId).get.channel push Json.toJson(json)
      }

    case Start(userId) =>
      usersTimes += (userId -> 0)
      
      for(a <- 1 to 1000) {
        receptionist ! Put("key"+a, "data"+a)
      }


    case SocketClosed(userId) =>


      val userChannel = webSockets.get(userId).get

      if (userChannel.channelsCount > 1) {
        userChannel.channelsCount = userChannel.channelsCount - 1
        webSockets += (userId -> userChannel)
      } else {
        removeUserChannel(userId)
        removeUserTimer(userId)
      }

  }

  def removeUserTimer(userId: Int) = usersTimes -= userId
  def removeUserChannel(userId: Int) = webSockets -= userId

}

sealed trait SocketMessage

case class StartSocket(userId: Int) extends SocketMessage

case class SocketClosed(userId: Int) extends SocketMessage

case class UpdateTime() extends SocketMessage

case class Start(userId: Int) extends SocketMessage

case class Stop(userId: Int) extends SocketMessage

case class NodeData(node: String, data: String) extends SocketMessage

