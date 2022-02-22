package week1

import java.util.concurrent._
import scala.util.DynamicVariable

package object LectureExample {
  val forkJoinPool = new ForkJoinPool
  val scheduler =
    new DynamicVariable[TaskScheduler](new DefaultTaskScheduler)

  /**
   * The definition of parallel.
   * The arguments are call by name.
   * For parallelism, need to pass unevaluated computations(call by name).
   */
  def parallel[A, B](taskA: => A, taskB: => B): (A, B) = {
    scheduler.value.parallel(taskA, taskB)
  }

  /**
   * The definition of parallel.
   * The arguments are call by name.
   * For parallelism, need to pass unevaluated computations(call by name).
   */
  def parallel[A, B, C, D](taskA: => A, taskB: => B, taskC: => C, taskD: => D): (A, B, C, D) = {
    val ta = task {
      taskA
    }
    val tb = task {
      taskB
    }
    val tc = task {
      taskC
    }
    val td = taskD
    (ta.join(), tb.join(), tc.join(), td)
  }

  def task[T](body: => T): ForkJoinTask[T] = {
    scheduler.value.schedule(body)
  }

  abstract class TaskScheduler {
    def schedule[T](body: => T): ForkJoinTask[T]

    def parallel[A, B](taskA: => A, taskB: => B): (A, B) = {
      val right = task {
        taskB
      }
      val left = taskA
      (left, right.join())
    }
  }

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

  class DefaultTaskScheduler extends TaskScheduler {
    def schedule[T](body: => T): ForkJoinTask[T] = {
      val t = new RecursiveTask[T] {
        def compute = body
      }
      Thread.currentThread match {
        case wt: ForkJoinWorkerThread =>
          t.fork()
        case _ =>
          forkJoinPool.execute(t)
      }
      t
    }
  }

}
