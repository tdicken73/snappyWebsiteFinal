package com.snappy.app

import Node.Get
import Node.Put
import Node.Result
import Organizer.DoneInit
import Organizer.Make
import akka.actor.Actor
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.event.Logging

object Receptionist {
  case class Store(data: String)
}

class Receptionist extends Actor {
  import Receptionist._
  import Organizer._
  import Node._

  val log = Logging(context.system, this)

  val organizer = context.actorOf(Props(new Organizer(3, 0, context.parent)), "organizer")
  organizer ! Make(10)

  def receive = {

    case Put(key, data) =>
      log.debug("recep got put:" + key + ", " + data)
      put(key, data)

    case Get(key) =>
      get(key)

    case DoneInit =>
      context.parent ! DoneInit

    case Result(key: String, result: String) =>
      context.parent ! Result(key, result)

  }

  def put(key: String, data: String) {
    organizer ! Put(key, data)
  }

  def get(key: String) {
    organizer ! Get(key)
  }

  def make(num: Integer) {
    organizer ! Make(num)
  }

}