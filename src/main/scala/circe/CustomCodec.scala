package circe

import io.circe.Decoder.Result
import io.circe.jawn.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, HCursor, Json, KeyDecoder, KeyEncoder}

case class User(id: Int, name: String, mailAddress: String)

object CustomeCodec extends App with CountrySupport {
  val user = User(16, "Austin", "aus@temp.com")
  val userJson = user.asJson.spaces2
  println(s"json: $userJson")

  val json = """ { "id": 3, "name": "Tom Hardy", "mail_address": "tom@abc.com" } """
  val decodedUser = decode[User](json)
  println(s"decode: $decodedUser")

}

trait CountrySupport {
  implicit val encodeCountry: Encoder[User] = new Encoder[User] {
    override def apply(a: User): Json = Json.obj(
      ("id", Json.fromInt(a.id)),
      ("name", Json.fromString(a.name)),
      ("mail_address", Json.fromString(a.mailAddress))
    )
  }

  implicit val decodeCountry: Decoder[User] = new Decoder[User] {
    override def apply(c: HCursor): Result[User] =
      for {
        id <- c.downField("id").as[Int]
        name <- c.downField("name").as[String]
        mailAddress <- c.downField("mail_address").as[String]
      } yield new User(id, name, mailAddress)
  }
}

/**
 * Custom Key Types
 *
 *  Provide a KeyEncodfer and/or KEyDecoder for your custom key type if encode/decode Map[K, V] where K is not String (or Symbol, Int, Long, etc.)
 */

case class Foo(value: String)

object CustomKeyType extends App with CustomKeyType  {

  val map = Map[Foo, Int] (
    Foo("hello") -> 123,
    Foo("world") -> 456
  )

  val json = map.asJson
  println(s"encode json: $json")


  val as = json.as[Map[Foo, Int]]
  println(s"decode: $as")

}

trait CustomKeyType {
  implicit val fooKeyEncoder: KeyEncoder[Foo] = new KeyEncoder[Foo] {
    override def apply(foo: Foo): String = foo.value
  }

  implicit val fooKeyDecoder: KeyDecoder[Foo] = new KeyDecoder[Foo] {
    override def apply(key: String): Option[Foo] = Some(Foo(key))
  }
}
