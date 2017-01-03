
import cats.data.Writer
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.writer._

object Factorial {
  def slowly[A](body: ⇒ A) = try body finally Thread.sleep(100)

  def factorial(n: Long): Long = {
    val ans = slowly(if (n == 0) 1 else n * factorial(n - 1))
    println(s" fact $n $ans")
    ans
  }

  def factorialWritter(n: Long): Logged[Long] = {
    val ans = factorial(n)
    ans.writer(Vector(s" fact $n=$ans"))
  }

  type Logged[A] = Writer[Vector[String], A]

  def fw(n: Long): Logged[Long] = {
    if (n == 0) {
      1L.pure[Logged]
    } else {
      for {
        a ← slowly(fw(n - 1))
        _ ← Vector(s"fact $n = ${a * n}").tell
      } yield a * n
    }
  }

}

