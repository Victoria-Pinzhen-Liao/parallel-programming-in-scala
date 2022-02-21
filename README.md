#  Parallel programming

:octocat: GitHub: [repo link](https://github.com/Victoria-Pinzhen-Liao/parallel-programming)
:page_facing_up:  blog link: https://purrgramming.life/cs/os/parallel

This is the code and lecture notes for coursera online course [Parallel programming](https://www.coursera.org/learn/scala2-parallel-programming/home/week/1) from École Polytechnique Fédérale de Lausanne

----

Paralleled programming is becoming more and more critical. Almost every desktop computer, laptop, or handheld device is equipped with multi-core processors and capable of executing computations in parallel. Therefore, it is more important than ever to harness these resources.  

 
![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645379507402.png)




## Definition

Parallel computing is a type of computation in which many calculations are performed at the **same time.** 

Parallel computing provides computational power when
sequential computing cannot do so


### Parallel  vs. Concurrent  

|          | Parallel                                                      | Concurrent                                                      |
|----------|---------------------------------------------------------------|-----------------------------------------------------------------|
| Def      | Uses parallel hardware to execute computation more quickly.   | May or **may not** execute multiple executions at the same time |
| Benefit  | Efficiency                                                   | Modularity,  Responsiveness,  Maintainability                                         


 ![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645379636346.png)

 
![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645379648601.png)
 

### Basic principle
- computation can be divided into smaller subproblems
- each of which can be solved **simultaneously** 

### Assumption
We have parallel hardware at our disposal, which can execute these computations in parallel.


## Trivia 
### History 

At the beginning of the 21st century, tech vendors provided multiple CPU cores on the same processor chip, each capable of executing separate instruction streams.

###  Parallel Hardware
- multi-core processors
- symmetric multiprocessors
- general-purpose graphics processing unit
- field-programmable gate arrays
- computer clusters

## Thread and Process

### Process
The process is an instance of a program executing in the OS. 

- The same program can be started as a process more than once or simultaneously in the same OS.
- The operating system multiplexes many different processes and a limited number of CPUs to get time slices of execution. This mechanism is called multitasking. 
- Two different processes cannot directly access each other’s **memory** – they are isolated.

 
![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645379519458.png)

### Thread

Each process can contain multiple independent concurrency units called threads. 

Each thread in JVM is created and controlled by a unique object of the `java.lang.Thread ` class.

- Threads can be started from within the same program, and they **share** the same memory address space. 
- Each thread has a program counter and a program stack. 
- JVM threads cannot modify each other’s **stack** memory.  They can only modify the **heap** memory.

![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645379546146.png)

 

!![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645379557382.png)

#### Main Thread
A user thread is automatically created to execute the `main()` method, i.e., the main thread.

#### Thread vs. Process

![Process vs Threads in Linux](https://linuxhint.com/wp-content/uploads/2021/10/Process-vs-Threads-in-Linux-2.png)

#### Thread states

![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645386184010.png)

See this ref for more info - [Thread States](https://www.cs.princeton.edu/courses/archive/spr96/cs333/java/tutorial/java/threads/states.html)

#### thread.join

Join is a synchronization method that blocks the calling thread (that is, the thread that calls the method) until the thread whose `join` method is completed. 
- Use this method to ensure that a thread has been terminated. 
- The caller will block indefinitely if the thread does not terminate.  

![JavaByPatel: Data structures and algorithms interview questions in Java:  How Thread.join() in Java works internally.](https://2.bp.blogspot.com/-EEy5kwEDcno/V0oGQfIOhqI/AAAAAAAABDo/aO99Pzsu5K0xiCjMc4aJ_CvXTqpS6XScACLcB/s1600/thread-join-method-in-java.png)






## Other Terms

### Operating system
Software that manages hardware and software resources and schedules program execution. 

### Parallelism Granularity
Parallelism manifests itself at different granularity levels.
- bit-level parallelism – processing multiple bits of data in parallel
- instruction-level parallelism – executing different instructions from the
same instruction stream in parallel
- task-level parallelism – executing separate instruction streams in
parallel  




## Creating and starting threads 

Each JVM process starts with the main thread. 
To start other threads: 
1. Define a Thread subclass. 
2. Instantiate a new Thread object. 
3. Call start on the Thread object. 

The Thread subclass defines the thread's code (`hello world`). 
The same custom Thread subclass can be used to start multiple threads.

```scala
class HelloWorld extends Thread {  
  override def run(): Unit = {  
  println("Hello world!")  
 }}  
  
object SingleThread extends App {  
  val t = new HelloThread  
  t.start()  
 t.join()  
}
```
Sys output

```scala
Hello, world!
```

## Atomicity

An operation is atomic if it appears as if it occurred instantaneously from the point of view of other threads. 

### Overlap Examples
The issue that atomicity can fix: Separate statements in two threads can overlap

```scala
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
  
object MultiThreadWithOverlap extends App {
  val tA = new HelloWorldA
  val tB = new HelloWorldB
  tA.start()
  tB.start()
  tA.join()
  tB.join()
}
```
Sys output
```scala
A1 Hello world!
A2 Hello world!
B1 Hello world!
A3 Hello world!
B2 Hello world!
B3 Hello world!
```

In some cases, we want to ensure that a sequence of statements in a specific thread executes at once. 

```scala
object MultiThreadWithOverlap2 extends App {
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

  def getUniqueId(): Long = {
    uidCount = uidCount + 1
    uidCount
  }

  startThread()
  startThread()
}

```
Sys output
```scala
Vector(1, 2, 4, 6, 8, 10, 12, 13, 15, 17)
Vector(1, 3, 5, 7, 9, 11, 12, 14, 16, 17)
```
### Synchronized Block 

Threads within a given process share the same memory space
 - pros: Enable data sharing 
 - cons: Threads might modify the same resource at the same time

**Access Control**: Code block after a synchronized call on an object x is never executed by two threads at the same time

i.e. Change the `getUniqueId` to 
```scala
val x = new AnyRef {}
def getUniqueId(): Long = x.synchronized {  
 uidCount = uidCount + 1  
  uidCount  
}
```
Full code

```scala
object SynchronizeExample extends App {
  val x = new AnyRef {}
  var uidCount = 0L

  def startThread()
  = {
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

```
Sys output
```scala
Vector(2, 12, 13, 14, 15, 16, 17, 18, 19, 20)
Vector(1, 3, 4, 5, 6, 7, 8, 9, 10, 11)
```
Where we can see that 
- Different threads use the synchronized block to agree on unique values. 
- The synchronized block is an example of synchronization primitive.

### Nesting  Synchronized Block 

Invocations of the synchronized block can nest
like 
```scala
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

```

## Memory model 

Memory model is a set of rules that describes how threads interact when accessing shared memory. 
Java Memory Model – the memory model for the JVM. 
1. Two threads writing to separate locations in memory do not need synchronization. 
2. A thread `X` that calls `join` on another thread `Y` is guaranteed to observe all the writes by thread `Y` after join returns.

## Deadlock 

### Definition
Deadlock: a thread is waiting for an object lock that another thread holds, and this second thread is waiting for an object lock that the first thread holds

i.e. each thread is waiting for the other thread to relinquish a lock  and wait forever  

![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645390781993.png)

### Condition


- **Mutual exclusion**: At least one resource must be held in a non-shareable mode; that is, only one process at a time can use the resource. 
- **Hold and wait or resource holding**: a process is currently holding at least one resource and requesting additional resources which are being held by other processes.
- **No preemption**: a resource can be released only voluntarily by the process holding it.
- **Circular wait**: each process must be waiting for a resource which is being held by another process, which in turn is waiting for the first process to release the resource.  

![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645463425065.png)


### Deadlock  Code Example


The amount of money of a bank account is locked (synchronized) when transferring money to make sure that number is correct. If we have 2 accounts , and both trying to send money to each other, then both accounts are locked.

Here, the two transfer threads compete for resources
(`amount` in the `Account`), and wait for each to finish without releasing
the already acquired resources.

```scala
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

```



Result:
```scala
The app would never end.
```

### Deadlock Handling

#### OS
- Most current operating systems **cannot** prevent deadlocks.
- When a deadlock occurs, different operating systems respond to them in different **non-standard manners**. 
- Most approaches work by preventing   the ***fourth*** condition  (Circular wait)

####  Ignoring deadlock
Ostrich algorithm: 
- In this approach, it is assumed that a deadlock will never occur. 
- Initially used by   UNIX.
- Used when the time intervals between occurrences of deadlocks are large and the data loss incurred each time is tolerable.
- Can be safely done if deadlocks are formally proven to never occur. 

####  Deadlock Prevention vs Deadlock Avoidance

 
Deadlock **prevention**  - ensure that at least one of the necessary conditions for deadlock can never occur. 

Deadlock **avoidance** -  ensure that the system does not enter an unsafe state. 

![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645473109837.png)


#### Deadlock Prevention

##### Summary 
 
 | Condition | Approach | Practical? |
| :---: | :---: |:---: |
| Mutual Exclusion | Spooling | x |
| Hold and Wait | Request for all the resources initially | x |
| No Preemption | Snatch all the resources | x |
| Circular Wait | Assign priority to each resources and order resources numerically | yes|

##### Details 
1. Removing the mutual exclusion condition means that no process will have exclusive access to a resource.  
   - Algorithm: non-blocking synchronization algorithms.
   - Not practical:
	  - This proves impossible for resources that cannot be spooled. 
	  - But even with spooled resources, the deadlock could still occur. 
	
2. The hold and wait or resource holding condition: These algorithms, such as serializing tokens, are known as the all-or-none algorithms.

	Algorithm 1: may be removed by requiring processes to request all the resources they will need before starting up or before embarking upon a particular set of operations.
	Not practical:
	- This advance knowledge is frequently difficult to satisfy 
	- This is an inefficient use of resources. 

	Algorithm 2:  require processes to request resources only when it has none; 
	Not practical:
   - Threads must release all their currently held resources before requesting all the resources they will need from scratch.  However, resources may be allocated and remain unused for long periods. 
   - A process requiring a popular resource may have to wait indefinitely, as such a resource may always be allocated to some process, resulting in resource starvation. 



3. The no preemption condition: inability to enforce preemption may interfere with a priority algorithm. 

Not practical:
- A process has to be able to have a resource for a certain amount of time, or the processing outcome may be inconsistent or thrashing may occur. 
- Preemption of a "locked out" resource generally implies a rollback, and is to be avoided since it is very costly in overhead. 

Algorithms 
- lock-free
- wait-free 
- optimistic concurrency control 



4. The circular wait condition: 
Algorithms  
-  Disabling interrupts during critical sections and using a hierarchy to determine a partial ordering of resources. If no obvious hierarchy exists, even the memory address of resources has been used to determine ordering and resources are requested in the increasing order of the enumeration.
- Dijkstra 


#### Deadlock avoidance

Deadlock avoidance does not impose any conditions as seen in prevention but, here each resource request is carefully analyzed to see whether it could be safely fulfilled without causing deadlock.

Deadlock avoidance requires that the operating system be given in advance additional information concerning which resources a process will request and use during its lifetime. 

**Algorithm:** [Banker's algorithm](https://en.wikipedia.org/wiki/Banker%27s_algorithm): analyzes each and every request by examining that there is no possibility of deadlock occurrence in the future if the requested resource is allocated. 


**Drawback**:   its requirement of information in advance about how resources are to be requested in the future. One of the most used deadlock avoidance algorithm is 
 
 #### Deadlock Solution Code Example 

For the example we used in the deadlock Code example, one approach is to always acquire resources in the same order. This assumes an ordering relationship on the resources. 

```scala
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

```

Sys output
 ```scala
After transfer: sender account: 2000 , receiver account:1000
After transfer: sender account: 980 , receiver account:2020

Process finished with exit code 0
 ```

 

------------

WIP 

![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645379600842.png)

## Reference

- (2022). Retrieved 18 January 2022, from https://www.coursera.org/learn/scala2-parallel-programming/lecture/vd3xo/course-overview  
- GitHub - ehsanmok/scala-parallel-programming: coursera. (2022). Retrieved 18 January 2022, from https://github.com/ehsanmok/scala-parallel-programming
- (2022). Retrieved 18 January 2022, from https://www.epfl.ch/labs/lamp/wp-content/uploads/2019/01/week01.pdf 
- Parallel Algorithm - Introduction. (2022). Retrieved 18 January 2022, from https://www.tutorialspoint.com/parallel_algorithm/parallel_algorithm_introduction.htm
- Thread State. (2022). Retrieved 18 February 2022, from https://www.cs.princeton.edu/courses/archive/spr96/cs333/java/tutorial/java/threads/states.html
