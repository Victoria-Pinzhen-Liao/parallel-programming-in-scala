package week1.LectureExample

import week1.LectureExample.PNormCalculator.{power, sumSegment}
import week1.LectureExample.SequentialPNormCalculator._

object SequentialPNormCalc extends App {
  val array = Array(1, 2, 3, 4, 5, 6, 7, 8)
  val p = 2.5
  sequentialPNormCalc(array, p)
}

object SequentialPNormCalculator {
  def sequentialPNormCalc(a: Array[Int], p: Double): Int = {
    val m = a.length / 2
    val (sum1, sum2) = (sumSegment(a, p, 0, m),
      sumSegment(a, p, m, a.length))
    val result = power(sum1 + sum2, 1 / p)
    println(s"The p norm of ${a.toSeq} is: $result")
    result
  }
}

