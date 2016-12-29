// the interface
trait CanFoo[A] {
  def foos(x: A): String
}

object W {
  //the class
  case class Wrapper(wrapped: String)
}
import W._
// the evidence
object CanFooInstance {
  implicit val foosWrapper = new CanFoo[Wrapper] {
    def foos(input: Wrapper) = input.wrapped
  }
}
object CanFoo {
  def apply[A: CanFoo]: CanFoo[A] = implicitly
}

// type class allows to sepparate the definition of class from implementation of the interface
object WrapperCanFoo {
  import CanFooInstance._
  import CanFoo._
  implicit class WrapperCanFooOps[A](input: A) {
    def foos(implicit c: CanFoo[A]): String =
      c.foos(input)
  }

}
