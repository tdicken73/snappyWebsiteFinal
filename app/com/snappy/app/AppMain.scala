package com.snappy.app

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging

class AppMain extends Actor {

  import Node._
  import Organizer._

  val log = Logging(context.system, this)

  val recep = context.actorOf(Props[Receptionist], "receptionist")
  log.debug("receptionist created")

  def receive = {
    case Result(key: String, result: String) =>
      log.debug("Result: " + result)
      println(result)

    case DoneInit =>
      log.debug("Done with inititialization")
      for (a <- 1 to 10000) {
        recep ! Put("key" + a, "value" + a)
      }

      recep ! Get("key1")
      recep ! Get("key700")
  }

}