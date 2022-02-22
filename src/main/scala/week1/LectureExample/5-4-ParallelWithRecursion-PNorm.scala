package week1.LectureExample

import week1.LectureExample.PNormCalculator.{power, sumSegment}
import week1.LectureExample.ParallelPNormCalculatorWithRecursion.pNormRecursion

object ParallelPNormCalcWithRecursion extends App {
  val array = Array(1, 2, 3, 4, 5, 6, 7, 8)
  val p = 2.5
  pNormRecursion(array, p)
}

object ParallelPNormCalculatorWithRecursion {

  def pNormRecursion(a: Array[Int], p: Double): Int = {
    val result = power(sumSegmentRecursive(a, p, 0, a.length, 2), 1 / p)
    println(s"The p norm of ${a.toSeq} is: $result")
    result
  }

  // like sumSegment but parallel and recursive
  def sumSegmentRecursive(a: Array[Int], p: Double, s: Int, t: Int, threshold: Int): Int = {
    if (t - s < threshold)
      sumSegment(a, p, s, t) // small segment: do it sequentially
    else {
      val m = s + (t - s) / 2
      val (sum1, sum2) = parallel(sumSegmentRecursive(a, p, s, m, threshold),
        sumSegmentRecursive(a, p, m, t, threshold))
      sum1 + sum2
    }
  }
}
