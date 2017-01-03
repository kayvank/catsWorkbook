import cats.data.State
import State._
import cats.syntax.applicative._

object PC {
  type CalcState[A] = State[List[Int], A]

  def operand(num: Int): CalcState[Int] =
    State[List[Int], Int] { stack ⇒
      (num :: stack, num)
    }

  def operator(f: (Int, Int) ⇒ Int): CalcState[Int] =
    State[List[Int], Int] {
      case a :: b :: tail ⇒
        val ans = f(a, b)
        (ans :: tail, ans)
      case _ ⇒ sys.error("Fail")
    }

  def evalOne(sym: String): CalcState[Int] =
    sym match {
      case "+" ⇒ operator(_ + _)
      case "-" ⇒ operator(_ - _)
      case "*" ⇒ operator(_ * _)
      case "/" ⇒ operator(_ / _)
      case num ⇒ operand(num.toInt)
    }
  def evalAll(input: List[String]): CalcState[Int] =
    input.foldLeft(0.pure[CalcState])((acc, cur) ⇒
      acc.flatMap(_ ⇒ evalOne(cur)))

}
object TestPC {
  import PC._

  val program = for {
    _ ← evalOne("1")
    _ ← evalOne("2")
    ans ← evalOne("+")
  } yield ans
  val (state, result) = program.run(Nil).value
}
