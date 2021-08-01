package circe

import cats.syntax.functor._
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import io.circe.syntax._

object ADT extends App {

  import circe.GenericDerivation._
  import io.circe.parser.decode

  val bee = decode[Event]("""{ "i": 1000 }""")
  println(s"decode: $bee")

  val json = (Bee(210)).asJson.spaces2
  println(s"encode: $json")
}

sealed trait Event

case class Bee(i: Int) extends Event

case class Gee(s: String) extends Event

case class Cha(c: Char) extends Event

case class Qux(values: List[String]) extends Event

// Note that we have to call widen (which is provided by Cats’s Functor syntax,
// which we bring into scope with the first import) on the decoders because the Decoder type class is not covariant.
object GenericDerivation {
  implicit val encodeEvent: Encoder[Event] = Encoder.instance {
    case bee@Bee(_) => bee.asJson
    case gee@Gee(_) => gee.asJson
    case cha@Cha(_) => cha.asJson
    case qux@Qux(_) => qux.asJson
  }

  implicit val decodeEvent: Decoder[Event] =
    List[Decoder[Event]](
      Decoder[Bee].widen,
      Decoder[Gee].widen,
      Decoder[Cha].widen,
      Decoder[Qux].widen
    ).reduceLeft(_ or _)
}

/*
  A more generic solution
  Avoid the fuss of writing out all the cases
 */

object ShapesDerivation extends App with ShapesDerivationSupport {

  import circe.GenericDerivation._
  import io.circe.parser.decode
  import io.circe.syntax._

  val deco = decode[Event]("""{ "i": 3500 }""")
  println(s"decode: $deco")

  val enco = (Bee(2500)).asJson.spaces2
  println(s"encode: $enco")
}


trait ShapesDerivationSupport {

  //  import io.circe.shapes

  import shapeless.{Coproduct, Generic}

  implicit def encodeAdtNoDiscr[A, Repr <: Coproduct]
  (implicit
   gen: Generic.Aux[A, Repr],
   encodeRepr: Encoder[Repr]
  ): Encoder[A] = encodeRepr.contramap(gen.to)

  implicit def decodeAdtNoDiscr[A, Repr <: Coproduct]
  (implicit
   gen: Generic.Aux[A, Repr],
   decodeRepr: Decoder[Repr]
  ): Decoder[A] = decodeRepr.map(gen.from)
}
