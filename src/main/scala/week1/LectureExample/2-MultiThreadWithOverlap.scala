package week1.LectureExample

object MultiThreadWithOverlap extends App {
  val tA = new HelloThreadA
  val tB = new HelloThreadB
  tA.start()
  tB.start()
  tA.join()
  tB.join()
}
