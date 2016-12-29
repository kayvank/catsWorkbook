import cats.Functor
import cats.syntax.functor._

sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object Tree {

  import cats.Functor
  import cats.syntax.functor._
  implicit val treeFunctor = new Functor[Tree] {
    def map[A, B](tree: Tree[A])(f: A ⇒ B): Tree[B] =
      tree match {
        case Branch(left, right) ⇒
          Branch(map(left)(f), map(right)(f))
        case Leaf(value) ⇒
          Leaf(f(value))
      }
  }
}
