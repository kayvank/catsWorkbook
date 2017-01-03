import java.util.Date
import cats._
import cats.data._
import cats.implicits._
import cats.Monoid
import cats.instances

object Bootstrap extends App {

  import E._
  import W._
  import CanFooInstance._
  import WrapperCanFoo._
  import Printable._
  import PrintableInstance._
  import PrintableSyntax._
  import CodecInstance._
  import cats.instances.all._
  import Tree._
  import Mon._
  import cats.Id
  import ExceptionStyle._

  println("Hello com.example.ch1!")

  Cat("aubry", 102, "ugly").print
  //  Cat("aubry", 102, "ugly").catShow
  Wrapper("hi there").foos
  implicit val WrapperShow: Show[Wrapper] = Show.show(w ⇒ s"${w.wrapped}----")
  println(s" --- ${Wrapper("hi again").show}")
  //  val eqInt = Eq[Int]
  implicit val dateEqual =
    Eq.instance[Date] {
      (date1, date2) ⇒ date1.getTime === date2.getTime
    }
  val cat1 = Cat("Garfield", 35, "orange and black")
  val cat2 = Cat("Heathcliff", 30, "orange and black")
  val ss = cat1 === cat2

  val so = (Option(cat1): Option[Cat]) === (Option(cat2): Option[Cat])
  println(s"cat1 === cat2 = ${ss}")
  println(s"optioncat1 === optioncat2 = ${so}")

  Monoid[String].combine("Hi ", "There")

  def add(items: List[Int]): Int = items.foldLeft(Monoid[Int].empty)((z, c) ⇒ z |+| c)

  println(s"add(List(3,5))=${add(List(3, 5))}")
  val tree = Branch(Leaf(10), Leaf(20))
  Functor[Tree].map(tree)(x ⇒ x * 2)
  format(Box("hello world"))
  format(Box(true))
  encode(Box(123))
  val r = stringDivideBy("10", "5")
  val x = sumSquare(Option(10), Option(4))
  val xid = sumSquare(3: Id[Int], 4: Id[Int])

  println(s"sumsQuareID = ${xid}")
  println(s"sumsQuare = ${x}")
  val ee = magic("15")

  magic("123") match {
    case Left(e)  ⇒ println(s"not a number! ${e.getMessage}")
    case Right(s) ⇒ println(s"Got reciprocal: ${s}")
  }

  import Jsonable._
  def convert2json[T](x: T)(implicit converter: Jsonable[T]): io.circe.Json = {
    converter.serialize(x)
  }
  val j: io.circe.Json = convert2json("hello world")

  import Jsonable1._
  implicit def convertTojson[T](implicit converter: Jsonable1[T]): io.circe.Json = converter.serialize
  val j1: io.circe.Json = convert2json("hello world")
  println(s" j1= ${j1}")

  import scala.concurrent._
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._
  import Factorial._

  val res =
    Await.result(Future.sequence(Vector(
      Future(fw(3)),
      Future(fw(4)),
      Future(fw(5))
    )), Duration.Inf)

  val resW = res.map(_.run)
  res.foreach(x ⇒
    println(s" iteration=${x.value} -> ${x.written}"))
}
