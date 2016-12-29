import io.circe._, io.circe.parser._
import cats.syntax.either._
trait Jsonable1[T] {
  def serialize: io.circe.Json
}
object Jsonable1 {
  implicit class StringJsonable(s: String) extends Jsonable1[String] {
    def serialize = parse(s).getOrElse(Json.Null)
  }

  implicit class DoubleJsonable(d: Double) extends Jsonable1[Double] {
    def serialize = io.circe.Json.fromDoubleOrNull(d)
  }

  implicit class Intsonable(i: Int) extends Jsonable1[Int] {
    def serialize = io.circe.Json.fromInt(i)
  }

}
trait Jsonable[T] {
  def serialize(t: T): io.circe.Json
}
object Jsonable {
  implicit object StringJsonable extends Jsonable[String] {
    def serialize(t: String) = Json.fromString(t)
  }
  implicit object DoubleJsonable extends Jsonable[Double] {
    def serialize(t: Double) = Json.fromDoubleOrNull(t)
  }
  implicit object IntJsonable extends Jsonable[Int] {
    def serialize(t: Int) = Json.fromInt(t)
  }
}
