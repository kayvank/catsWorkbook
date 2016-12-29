/**
 * cats chapter 4 excersize & bool samples
 */

import scala.language.higherKinds
import cats.Monad
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.data.EitherT

/**
 * * Monad is a controling mechanism for sequencing computation
 */
trait Mon[F[_]] {
  def pure[A](value: A): F[A] // abstracts over construction
  def flatMap[A, B](value: F[A])(f: A ⇒ F[B]): F[B] // provides sequencing steps
  def map[A, B](value: F[A])(f: A ⇒ B): F[B] = {
    flatMap(value)(a ⇒ pure(f(a)))
  }
}
import cats.Id
object MonInstance {
  // constructor
  def pure[A](value: A): Id[A] = value
  def map[A, B](initial: Id[A])(f: A ⇒ B): Id[B] = f(initial)
  def flatMap[A, B](initial: Id[A])(f: A ⇒ Id[B]): Id[B] = f(initial)
}

object Mon {
  def parseInt(str: String): Option[Int] =
    scala.util.Try(str.toInt).toOption

  def divide(a: Int, b: Int): Option[Int] =
    if (b == 0) None else Some(a / b)

  def stringDivideBy(str1: String, str2: String) =
    parseInt(str1).flatMap(n ⇒
      parseInt(str2).flatMap(m ⇒
        divide(n, m)))

  def stringDivideBy2(str1: String, str2: String) =
    for {
      a1 ← parseInt(str1)
      a2 ← parseInt(str2)
      r ← divide(a1, a2)
    } yield (r)

  def sumSquare2[M[_]: Monad](a: M[Int], b: M[Int]): M[Int] =
    a.flatMap(x ⇒ b.map(y ⇒ x * x + y * y))

  def sumSquare[M[_]: Monad](a: M[Int], b: M[Int]): M[Int] = for {
    x ← a
    y ← b
  } yield x * x + y * y
  import cats.Eval

  def foldRight[A, B](as: List[A], acc: B)(f: (A, B) ⇒ B): B =
    foldRightEval(as, Eval.now(acc))((a, b) ⇒ b.map(f(a, _))).value

  def foldRightEval[A, B](as: List[A], acc: Eval[B])(f: (A, Eval[B]) ⇒ Eval[B]): Eval[B] = as match {
    case head :: tail ⇒ Eval.defer(foldRight(tail, acc)(f))
    case Nil          ⇒ acc
  }
}
