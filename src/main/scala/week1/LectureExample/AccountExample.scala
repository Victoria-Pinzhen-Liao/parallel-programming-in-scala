package week1.LectureExample

import week1.LectureExample.Account

class Account(private var amount: Int = 0) {
  def sendMoney(receiver: Account, n: Int) = {
    this.synchronized {
      receiver.synchronized {
        this.amount -= n
        receiver.amount += n
      }
    }
    println(s"After transfer: sender account: ${this.amount} , receiver " +
      s"account:${receiver.amount}")
  }
}

object AccountExample extends App {
  val sender = new Account(1994)
  val receiver = new Account(5)

  val t1 = transfer(sender, receiver, 20)
  val t2 = transfer(sender, receiver, 20)

  t1.join()
  t2.join()

  def transfer(sender: Account, receiver: Account, amount: Int) = {
    val t = new Thread {
      override def run(): Unit = {
        sender.sendMoney(receiver, amount)
      }
    }
    t.start()
    t
  }
}
