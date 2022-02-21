package week1.LectureExample

object MultiThreadWithOverlap extends App {
  val tA = new HelloWorldA
  val tB = new HelloWorldB
  tA.start()
  tB.start()
  tA.join()
  tB.join()
}
