import cats.data.Reader
import cats.syntax.applicative._

object DBMonad {

  case class Db(
    usernames: Map[Int, String],
    passwords: Map[String, String]
  )

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(db ⇒ db.usernames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader(db ⇒ db.passwords.exists(_ == (username → password)))

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    for {
      u ← findUsername(userId)
      p ← u.map(checkPassword(_, password)).getOrElse { false.pure[DbReader] }
    } yield p
}

