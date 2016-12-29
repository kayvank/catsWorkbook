final case class Person(name: String, email: String)

object JsonWriterInstances {
  implicit val stringJsonWriter = new JsonWriter[String] {
    def write(value: String): Json = JsString(value)
  }
  implicit val personJsonWriter = new JsonWriter[Person] {
    def write(value: Person) = {
      JsObject(Map(
        "name" → JsString(value.name),
        "email" → JsString(value.email)
      ))
    }
  }
}
