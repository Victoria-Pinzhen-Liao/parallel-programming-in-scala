package week1.LectureExample

// Program will sometimes terminate.

class Account(var balance: Int = 0) {
  def sendMoney(receiver: DeadlockFreeAccount, n: Int) = {
    this.synchronized {
      receiver.synchronized {
        this.balance -= n
        receiver.balance += n
      }
    }
  }
}

object DeadlockExample extends App {
  val account1 = new DeadlockFreeAccount(2000)
  val account2 = new DeadlockFreeAccount(1000)

  // account 1 -> account 2, and
  // account 2 -> account 1
  val transfer1 = transferThread(account1, account2, 20)
  val transfer2 = transferThread(account2, account1, 20)

  transfer1.join()
  transfer2.join()

  def transferThread(sender: DeadlockFreeAccount, receiver: DeadlockFreeAccount, amount: Int) = {
    val t = new Thread {
      override def run(): Unit = {
        // Loop: make it easier to trigger deadlock
        for (i <- 0 until amount) {
          sender.sendMoney(receiver, 1)
        }
        println(s"After transfer: sender account: ${sender.balance} , receiver " +
          s"account:${receiver.balance}")
      }
    }
    t.start()
    t
  }
}
