package circe

import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.semiauto._
import io.circe.syntax._


case class SemiAuto(a: Int, b: String, c: Boolean)

object SemiAutomaticDerivation extends App {
  val json = SemiAuto(12, "Auto", true).asJson
  println(json)

}

object SemiAuto {
  implicit val semiDecoder2: Decoder[SemiAuto] = deriveDecoder
  implicit val semiEncoder2: Encoder[SemiAuto] = deriveEncoder
  // OR
  //  implicit val semiDecoder: Decoder[Automatic] = deriveDecoder[Automatic]
  //  implicit val semiEncoder: Encoder[Automatic] = deriveEncoder[Automatic]
}


import io.circe.generic.auto._

case class Person(name: String)
case class Greeting(salutation: String, person: Person, exclamationMarks: Int)

object AutomaticDerivation extends App {

  val greeting = Greeting("Hey", Person("Chris"), 3).asJson
  println(greeting)
}