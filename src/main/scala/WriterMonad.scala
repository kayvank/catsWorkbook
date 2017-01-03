import cats.data.Writer
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.writer._

object WMonad {
  type Logged[A] = Writer[Vector[String], A]
  val noLog = 123.pure[Logged]
  val vv = Vector("msg1", "msg2").tell
  val a = Writer(123, Vector("msg1", "msg2", "msg3"))
  val (result, log) = a.run

  val writer1 = for {
    a1 ← 10.pure[Logged]
    _ ← Vector("x", "y", "z").tell
    _ ← Vector("a", "b", "c").tell
    a2 ← 32.writer(Vector("u", "v", "w"))
  } yield (a1 + a2)

  val w1 = writer1.run
  val writer2 = writer1.mapWritten(_.map(_.toUpperCase))
  val w2: cats.Id[(scala.collection.immutable.Vector[String], Int)] = writer2.run
  val w3 = writer1.bimap(
    log ⇒ log.map(_.toUpperCase),
    r ⇒ r * 100
  )
}

