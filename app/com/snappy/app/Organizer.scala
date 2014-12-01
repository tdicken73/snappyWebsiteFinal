package com.snappy.app

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import scala.collection.mutable.HashMap
import akka.event.Logging

object Organizer {
  case class Make(num: Integer)
  case class DoneInit
}

class Organizer(backups: Integer, initialNodes: Integer) extends Actor {
  import Organizer._
  import Receptionist._
  import Node._
  
  val log = Logging(context.system, this)

  // List of current nodes
  var children = List.empty[ActorRef]
  var whoIsUp: List[ActorRef] = null
  var globalNodeCount = 0;
  val hashmap = new HashMap[String, List[ActorRef]]

  // Create initial nodes
  children = make(initialNodes, children)
  context.parent ! DoneInit

  // Used to get a new node
  def nodeProps(): Props = Props(new Node())

  def receive = {
    case Make(num: Integer) =>
      children = make(num, children)
      if (whoIsUp == null) whoIsUp = children

    case Get(key: String) =>
      log.debug("Organizer got get: "+key)
      get(key)

    case Put(key: String, value: String) =>
      log.debug("Organizer got put: "+key+", "+value)
      put(key, value)
      
    case Result(key: String, value: String) =>
      log.debug("Organizer got result "+key+", "+value)
      context.parent ! Result(key, value)
  }

  def make(num: Integer, current: List[ActorRef]): List[ActorRef] = {
    if (num == 0) current
    else {
      globalNodeCount = globalNodeCount + 1
      make(num - 1, current ::: List(context.actorOf(nodeProps(), "node"+globalNodeCount)))
    }
  }

  def get(key: String) {
    val ref = hashmap.get(key) match {
      case Some(s) => s
      case None => null
    }

    // Keep set of things waiting for, when we get back, we are fine
    
    
    // This shouldn't always be head
    if (ref != null) {
      ref.head ! Get(key)
      ref.tail.head ! Get(key)
      ref.tail.tail.head ! Get(key)
    }
  }

  def put(key: String, value: String) {
    val nodeList: List[ActorRef] = sendPuts(backups, key, value, List())
    hashmap += key -> nodeList
  }

  def sendPuts(num: Integer, key: String, value: String, list: List[ActorRef]): List[ActorRef] = {
    if (num == 0) list
    else {
      val x: ActorRef = nextUp
      x ! Put(key, value)
      sendPuts(num-1, key, value, list ::: List(x))
    }

  }

  def nextUp(): ActorRef = {
    if (whoIsUp == null || whoIsUp.isEmpty) whoIsUp = children
    val retVal = whoIsUp.head
    whoIsUp = whoIsUp.tail
    retVal
  }

}