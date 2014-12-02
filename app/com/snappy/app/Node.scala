package com.snappy.app

import java.io.File
import org.fusesource.lmdbjni.Database
import org.fusesource.lmdbjni.Env
import akka.actor.Actor
import akka.event.Logging
import java.nio.file.Files
import java.nio.file.Paths
import akka.actor.ActorRef
import actors.NodeData

object Node {
  case class Put(key: String, data: String)
  case class Get(key: String)
  case class Result(key: String, result: String)
  case class Log(node: String, logs: String)
  case object Stop
  case object Done
  case object Fail
}

class Node(logRef: ActorRef) extends Actor {
  import Node._

  val log = Logging(context.system, this)

  
  
  val name = self.path.name
  var env = new Env();

  log.info(name+" created")
  
  def receive = {
    case Put(key, data) =>
      logRef ! NodeData(name, "Store "+key+", " + data)

      if (storeData(key, data)) sender ! Done
      else sender ! Fail
    case Get(key) =>
      sender ! Result(key, getData(key))
      
    case Stop =>
      env.close()
      context.stop(self)
  }
  
  def storeData(key: String, data: String): Boolean = {
    var env = new Env();
    if (!Files.exists(Paths.get("/tmp/" + name))) new File("/tmp/" + name).mkdir()
    try {
      env.open("/tmp/" + name);
      var db = env.openDatabase(name);
      db.put(key.getBytes(), data.getBytes())

      db.close();
    } finally {
      // Make sure you close the env to avoid resource leaks.
      env.close();
    }
    true
  }

  def getData(key: String): String = {
    var env = new Env();
    if (!Files.exists(Paths.get("/tmp/" + name))) new File("/tmp/" + name).mkdir()

    try {
      env.open("/tmp/" + name);
      var db = env.openDatabase(name);

      val result = new String(db.get(key.getBytes()))
      log.debug("fetched "+result)
      db.close();
      result
    } finally {
      // Make sure you close the env to avoid resource leaks.
      env.close();
    }

  }


}