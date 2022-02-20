package week1.LectureExample

class HelloThread extends Thread {
  override def run(): Unit = {
    println("Hello world!")
  }
}

object SingleThread extends App {
  val t = new HelloThreadA
  t.start()
  t.join()
}
