package circe

import io.circe.parser.decode
import io.circe.syntax._

object EncodeDecode extends App {

  // Encode data to JSON using .asJson syntax
  val intsJson = List(1, 2, 3).asJson
  println(s"encode .asJson: $intsJson")

  // .as syntax for decoding data from Json
  val as = intsJson.as[List[Int]]
  println(s"decode .as: $as")

  // The decode function from the included [parser] module can be used to directly decode a JSON String
  val decoded = decode[List[Int]]("[1, 2, 3]")
  println(s"decode function: $decoded")

}
