trait Codec[A] {
  def encode(value: A): String

  def decode(value: String): Option[A]

  def imap[B](f: A ⇒ B, fInv: B ⇒ A): Codec[B] = {
    val self = this
    new Codec[B] {
      def encode(value: B): String = self.encode(fInv(value))

      def decode(value: String): Option[B] = self.decode(value).map(f)
    }
  }
}

import scala.util._

object CodecInstance {
  def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)

  def decode[A](value: String)(implicit c: Codec[A]): Option[A] = c.decode(value)

  implicit val intCodec = new Codec[Int] {
    def encode(value: Int): String = value.toString

    def decode(value: String): Option[Int] = Try(value.toInt).toOption
  }
  implicit val stringCodec = new Codec[String] {
    def encode(value: String): String = value

    def decode(value: String): Option[String] = Some(value)
  }

  import E._

  implicit def boxCodec[A](implicit c: Codec[A]) = c.imap[Box[A]](Box(_), _.value)
}
