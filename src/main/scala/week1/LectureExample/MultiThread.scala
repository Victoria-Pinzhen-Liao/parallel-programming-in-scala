package week1.LectureExample

import week1.LectureExample.HelloThreadA

class HelloThread extends Thread {
  override def run(): Unit = {
    println("Hello world!")
  }
}

object MultiThread extends App {
  val t = new HelloThreadA
  t.start()
  t.join()
}
