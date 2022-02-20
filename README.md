# parallel-programming

This is the code for coursera online course [Parallel programming](https://www.coursera.org/learn/scala2-parallel-programming/home/week/1) from 
École Polytechnique Fédérale de Lausanne

I wrote a blog for this course as well, see https://github.com/Victoria-Pinzhen-Liao/blog_parallel-programming


#  Parallel programming

:octocat: All of the code in this blog can be found on [GitHub](https://github.com/Victoria-Pinzhen-Liao/parallel-programming)

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
class HelloThread extends Thread {  
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
package parallel  
  
class HelloThreadA extends Thread {  
  override def run(): Unit = {  
  println("A1 Hello world!")  
  println("A2 Hello world!")  
  println("A3 Hello world!")  
 }}  
  
class HelloThreadB extends Thread {  
  override def run(): Unit = {  
  println("B1 Hello world!")  
  println("B2 Hello world!")  
  println("B3 Hello world!")  
 }}  
  
object MultiThreadOverlap extends App {  
  val tA = new HelloThreadA  
  val tB = new HelloThreadB  
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
var uidCount = 0L  
  
def startThread()  = {  
  val t = new Thread {  
  override def run(): Unit = {  
  val uids = for (i <- 1 to 10) yield getUniqueId()  
  println(uids)  
 } }  t.start()  
  t  
}  
  
def getUniqueId(): Long = {  
 uidCount = uidCount + 1  
  uidCount  
}  
  
startThread()  
startThread()
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
def sendMoney(receiver: Account, n: Int) = {  
  this.synchronized {  
  receiver.synchronized {  
  this.amount -= n  
      receiver.amount += n  
    }  
 }  println(s"After transfer: sender account: ${this.amount} , receiver " +  
    s"account:${receiver.amount}")  
}
```

## Deadlock 

### Definition
Deadlock: a thread is waiting for an object lock that another thread holds, and this second thread is waiting for an object lock that the first thread holds

i.e. each thread is waiting for the other thread to relinquish a lock  and wait forever  

![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645390781993.png)

------------

WIP 

![file](https://purrgramming.life/wp-content/uploads/2022/02/image-1645379600842.png)

## Reference

- (2022). Retrieved 18 January 2022, from https://www.coursera.org/learn/scala2-parallel-programming/lecture/vd3xo/course-overview  
- GitHub - ehsanmok/scala-parallel-programming: coursera. (2022). Retrieved 18 January 2022, from https://github.com/ehsanmok/scala-parallel-programming
- (2022). Retrieved 18 January 2022, from https://www.epfl.ch/labs/lamp/wp-content/uploads/2019/01/week01.pdf 
- Parallel Algorithm - Introduction. (2022). Retrieved 18 January 2022, from https://www.tutorialspoint.com/parallel_algorithm/parallel_algorithm_introduction.htm
- Thread State. (2022). Retrieved 18 February 2022, from https://www.cs.princeton.edu/courses/archive/spr96/cs333/java/tutorial/java/threads/states.html
