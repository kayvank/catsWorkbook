

object E {
  final case class Box[A](value: A)
  final case class Cat(name: String, age: Int, color: String)
  import cats._
  //  import cats.data._
  // import cats.instances.all._
  import cats.implicits._

  implicit val catEqual =
    Eq.instance[Cat] {
      (c1, c2) ⇒
        (
          c1.name === c2.name &&
          c1.age === c2.age &&
          c1.color === c2.color
        )
    }
}
import E._

trait Printable[A] {
  def format(value: A): String
  def contracmap[B](f: B ⇒ A): Printable[B] = {
    val self = this
    new Printable[B] {
      def format(value: B): String = self.format(f(value))
    }
  }
}

object PrintableInstance {
  implicit def boxPrinrable[A](implicit p: Printable[A]) =
    p.contracmap[Box[A]](_.value)
  implicit val stringPrintable = new Printable[String] {
    def format(value: String): String = value
  }

  implicit val intPrintable = new Printable[Int] {
    def format(value: Int): String = value.toString
  }
  implicit val boolPritable = new Printable[Boolean] {
    def format(value: Boolean): String = value.toString
  }

  implicit val catPrintable = new Printable[Cat] {
    def format(cat: Cat) = s"NAME is ${cat.name} AGE ${cat.age} COLOR ${cat.color}"
  }
}

object Printable {
  import E._
  def format[A](value: A)(implicit p: Printable[A]): String =
    p.format(value)
  def print[A](value: A)(implicit p: Printable[A]): Unit = {
    println(format(value))
  }
}

object PrintableSyntax {
  implicit class PrintOps[A](value: A) {
    def format(implicit p: Printable[A]): String =
      p.format(value)
    def print(implicit p: Printable[A]): Unit =
      println(s"=====${format}")
  }
}
