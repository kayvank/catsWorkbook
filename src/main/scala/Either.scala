import cats._, cats.instances.all._, cats.syntax.eq._
import scala.util.Either._

object ExceptionStyle {
  def parse(s: String): Either[Throwable, Int] =
    if (s.matches("-?[0-9]+"))
      Right[Throwable, Int](s.toInt)
    else
      Left[Throwable, Int](new NumberFormatException(s"${s} is not a valid integer."))
  def reciprocal(i: Int): Either[Throwable, Double] =
    if (i == 0)
      Left(new IllegalArgumentException("Cannot take reciprocal of 0."))
    else Right(1.0 / i)

  def stringify(d: Double): String = d.toString

  def magic(s: String): Either[Throwable, String] = parse(s).flatMap(reciprocal).map(stringify)
}
