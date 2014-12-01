package com.snappy.app

import akka.actor.Actor

class Logger extends Actor{
  import Node._
  
  
  def receive = {
    
    case Log(node: String, logs: String) => 
    
  }

}