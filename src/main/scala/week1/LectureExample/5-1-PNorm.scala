package week1.LectureExample

import week1.LectureExample.PNormCalculator.pNorm

import scala.math.abs

object PNorm extends App {
  val array = Array(1, 2, 3, 4, 5, 6, 7, 8)
  val p = 2.5
  pNorm(array, p)
}

object PNormCalculator {
  def pNorm(a: Array[Int], p: Double): Int = {
    val result = power(sumSegment(a, p, 0, a.size), 1 / p)
    println(s"The p norm of ${a.toSeq} (p = $p) is: $result")
    result
  }

  def sumSegment(a: Array[Int], p: Double, s: Int, t: Int): Int = {
    var i = s
    var sum: Int = 0
    while (i < t) {
      sum = sum + power(a(i), p)
      i = i + 1
    }
    sum
  }

  def power(x: Int, p: Double): Int = math.exp(p * math.log(abs(x))).toInt
}

