package circe

import cats.syntax.either._
import io.circe.Decoder.Result
import io.circe._
import io.circe.parser._

object ExtractJson extends App {

  val json: String =
    """
      | {
      |   "id": "x07dD-0c23-8t43-258c90bq01",
      |   "name": "Foo",
      |   "counts": [ 1, 2, 3 ],
      |   "values": {
      |     "bar": true,
      |     "baz": 100.001,
      |     "qux": [ "a", "b" ]
      |   }
      | }
      |""".stripMargin

  val doc: Json = parse(json).getOrElse(Json.Null)
  println(s"doc -> $doc")


  /** Extracting Data */
  val cursor: HCursor = doc.hcursor
  val baz: Decoder.Result[Double] = cursor.downField("values").downField("baz").as[Double]
  val baz2: Decoder.Result[Double] = cursor.downField("values").get[Double]("baz")
  val quxList: Result[List[String]] = cursor.downField("values").downField("qux").as[List[String]]
  // If the focus is a JSON array, move to its first element.
  val secondQux: Result[String] = cursor.downField("values").downField("qux").downArray.as[String]

  println(
    s"""HCursor:
       | baz -> $baz
       | baz2 -> $baz2
       | quxList -> $quxList
       | secondQux -> $secondQux
       |""".stripMargin
  )


  /** Transforming Data */
  val reversedNameCursor: ACursor = cursor.downField("name").withFocus(_.mapString(_.reverse))
  println(s"nameField.as[String]: ${reversedNameCursor.as[String]}")
  val reverseName:Option[Json] = reversedNameCursor.top
  println(s"top: $reverseName")

}
