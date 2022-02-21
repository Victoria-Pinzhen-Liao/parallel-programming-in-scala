package week1

package object LectureExample {
  class HelloWorldA extends Thread {
    override def run(): Unit = {
      println("A1 Hello world!")
      println("A2 Hello world!")
      println("A3 Hello world!")
    }
  }

  class HelloWorldB extends Thread {
    override def run(): Unit = {
      println("B1 Hello world!")
      println("B2 Hello world!")
      println("B3 Hello world!")
    }
  }
}
