package circe

import io.circe.Encoder
import io.circe.generic.JsonCodec
import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec, JsonKey}
import io.circe.syntax._

/*
  This works with both case classes and sealed trait hierarchies
  NOTE: You will need to use the -Ymacro-annotations flag to use annotation macros like @JsonCodec.
  (If you’re using Scala 2.10.x to Scala 2.12.x you will need the Macro Paradise plugin instead).
*/

//@JsonCodec case class Codec(i: Int, s: String)
//
//object JsonCodecDerivation extends App {
//
//  // JsonCodec
//  val json = Codec(12, "Qux").asJson
//  println(json)
//}
//
//@ConfiguredJsonCodec case class Account(firstName: String, lastName: String)
//
//@ConfiguredJsonCodec case class Bar(@JsonKey("my-int") i: Int, s: String)
//// OR
////@ConfiguredJsonCodec case class Bar(i: Int, s: String)
//
//object ConfigJsonCodec extends App with ConfiguredJsonCodesSupport {
//  val account = Account("Dustin", "Moran").asJson
//  println(s"codec: $account")
//
//  val bar = Bar(13, "Qux").asJson
//  println(s"$bar")
//
//}
//
//trait ConfiguredJsonCodesSupport {
//  // In many cases the transformation is as simple as going from camel case to snake case, in which case all you need is a custom implicit configuration:
//  implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames
//
//  /*
//    In other cases you may need more complex mappings. These can be provided as a function:
//    Same as below
//    @ConfiguredJsonCodec case class Bar(@JsonKey("my-int") i: Int, s: String)
//   */
//  //  implicit val config2: Configuration = Configuration.default.copy(
//  //    transformMemberNames = {
//  //      case 'i' => "my-int"
//  //      case other => other
//  //    }
//  //  )
//}


/*
  Using forProductN
 */
case class U(firstName: String, lastName: String)

case class B(i: Int, s: String)

object ForProduct extends App with ForProductSupport {
  val u = U("Dustin", "Moran").asJson
  println(s"u: $u")

  val b = B(13, "Qux").asJson
  println(s"b: $b")

}

trait ForProductSupport {
  implicit val encodeUser: Encoder[U] =
    Encoder.forProduct2("first_name", "last_name")(u => (u.firstName, u.lastName))

  implicit val encodeBar: Encoder[B] =
    Encoder.forProduct2("my-int", "s")(b => (b.i, b.s))
}