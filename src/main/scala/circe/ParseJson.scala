package circe

import io.circe._
import io.circe.parser._

object ParseJson extends App {
  val rawJson: String =
    """
      | {
      |   "foo": "bar",
      |   "baz": 123,
      |   "list of stuff": [ 4, 5, 6 ]
      | }
      |""".stripMargin

  val parseResult = parse(rawJson)

  // parseResult: Either[ParsingFailure, Json] = Right(
  //   value = JObject(
  //     value = object[foo -> "bar",baz -> 123,list of stuff -> [
  //   4,
  //   5,
  //   6
  // ]]
  //   )
  // )

  parseResult match {
    case Right(json) => println(s"Right(json) => $json")
    case Left(e) => println(s"error: $e")
  }
  val json = parseResult.getOrElse(Json.Null)
  println((s"getOrElse =>  $json"))

//  val doc: Json = parseResult.getOrElse(Json.Null)
  val cursor: HCursor = json.hcursor
}
