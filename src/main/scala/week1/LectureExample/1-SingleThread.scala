package week1.LectureExample

object SingleThread extends App {
  val t = new HelloWorldA
  t.start()
  t.join()
}
