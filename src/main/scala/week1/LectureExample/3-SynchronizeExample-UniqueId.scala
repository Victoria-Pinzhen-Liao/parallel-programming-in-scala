package week1.LectureExample

object SynchronizeExample extends App {
  val x = new AnyRef {}
  var uidCount = 0L

  def startThread() = {
    val t = new Thread {
      override def run(): Unit = {
        val uids = for (i <- 1 to 10) yield getUniqueId()
        println(uids)
      }
    }
    t.start()
    t
  }

  def getUniqueId(): Long = x.synchronized {
    uidCount = uidCount + 1
    uidCount
  }

  startThread()
  startThread()
}
