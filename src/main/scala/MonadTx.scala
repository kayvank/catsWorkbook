import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import cats.data.OptionT
import cats.implicits._

object MT {

  val customGreeting: Future[Option[String]] = Future.successful(Some("Wecome basename Lola"))
  val excitedGreeting: Future[Option[String]] = customGreeting.map(_.map(_ + "!"))
  val hasWelcome: Future[Option[String]] = customGreeting.map(_.filter(_.contains("welcome")))
  val noWelcome: Future[Option[String]] = customGreeting.map(_.filterNot(_.contains("welcome")))
  val withFallBack: Future[String] = customGreeting.map(_.getOrElse("hello, there!"))

  val customGreetingT: OptionT[Future, String] = OptionT(customGreeting)
  val excitedGreetingT: OptionT[Future, String] = customGreetingT map (_ + "!")
  val hasWelcomeT: OptionT[Future, String] = customGreetingT filter (_.contains("welcom"))
  val noWelcomeT: OptionT[Future, String] = customGreetingT filterNot (_.contains("welcome"))
  val withFallBackT: Future[String] = customGreetingT.getOrElse("hello, there!")

  val greetingFO: Future[Option[String]] = Future.successful(Some("Hello"))
  val firstnameF: Future[String] = Future.successful("Jane")
  val lastnameO: Option[String] = Some("Doe")

  val ot: OptionT[Future, String] = for {
    g ← OptionT(greetingFO)
    h ← OptionT.liftF(firstnameF)
    l ← OptionT.fromOption[Future](lastnameO)
  } yield s" $g $h $l"

  val result: Future[Option[String]] = ot.value
}
