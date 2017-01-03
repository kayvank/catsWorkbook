import cats.data.Reader

object RM {

  case class Cat(name: String, favFood: String)
  case class Kaz(name: String, age: Int)

  val catName: Reader[Cat, String] = Reader(cat ⇒ cat.name)
  val catNameRun = catName.run(Cat("Garfield", "lasagne"))

  val greetKitty: Reader[Cat, String] = catName.map(name ⇒ s"Hello ${name}")
  val feetKitty: Reader[Cat, String] = Reader(cat ⇒ s"Have a nice bowl of ${cat.favFood}")
  val greetAndFeed: Reader[Cat, String] =
    for {
      msg1 ← greetKitty
      msg2 ← feetKitty
    } yield (s"${msg1} ${msg2}")
  val catWoman = Cat("MichellePfiffer", "toxic-plants")
  val gafCW = greetAndFeed(catWoman)

  val greetKittyRun = greetKitty.run(Cat("Aubry", "toilet water"))

  val kazName: Reader[Kaz, Int] = Reader(kaz ⇒ kaz.age - 10)

  val ageDec = kazName.run
  val youngerKayvan = ageDec(Kaz("Kayvan", 53))

}
