package week1.LectureExample

import week1.LectureExample.{HelloThreadA, HelloThreadB}

class HelloThreadA extends Thread {
  override def run(): Unit = {
    println("A1 Hello world!")
    println("A2 Hello world!")
    println("A3 Hello world!")
  }
}

class HelloThreadB extends Thread {
  override def run(): Unit = {
    println("B1 Hello world!")
    println("B2 Hello world!")
    println("B3 Hello world!")
  }
}

object MultiThreadOverlap extends App {
  val tA = new HelloThreadA
  val tB = new HelloThreadB
  tA.start()
  tB.start()
  tA.join()
  tB.join()
}
