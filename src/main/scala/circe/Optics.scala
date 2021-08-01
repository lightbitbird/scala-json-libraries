package circe

import io.circe.Json
import io.circe.optics.JsonPath._
import io.circe.optics.all.jsonPlated
import io.circe.parser._
import monocle.function.Plated

object Optics extends App {

  val json: Json = parse(
    """
       {
         "order": {
           "customer": {
             "name": "Custy McCustomer",
             "contact_details": {
               "address": "1 Fake Street, London, England",
               "phone": "0123-456-789"
             }
           },
           "items": [{
             "id": 123,
             "description": "banana",
             "quantity": 1
           }, {
             "id": 456,
             "description": "apple",
             "quantity": 2
           }],
           "total": 123.45
         }
       }
      """).getOrElse(Json.Null)

  val phoneNumberFromCursor: Option[String] = json.hcursor
    .downField("order")
    .downField("customer")
    .downField("contact_details")
    .get[String]("phone")
    .toOption

  println(s"phoneNumberFromCursor: $phoneNumberFromCursor")

  // get items vector
  val itemsFromCursor: Vector[Json] = json.hcursor
    .downField("order")
    .downField("items")
    .focus
    .flatMap(_.asArray)
    .getOrElse(Vector.empty)

  println(s"itemsFromCursor: $itemsFromCursor")

  // get the Vector quantities of all the items
  val quantities: Vector[Int] = itemsFromCursor.flatMap(_.hcursor.get[Int]("quantity").toOption)
  println(s"quantities: $quantities")

  // get the quantities with optics
  val quantityList = root.order.items.each.quantity.int.getAll(json)
  println(s"quantityList: $quantityList")

  // get unit property from json
  val _address = root.order.customer.contact_details.address.string
  val address = _address.getOption(json)
  println(s"address: $address")

  val getAddress2: Option[String] = root.order.customer.contact_details.address.string.getOption(json)
  val address2 = getAddress2
  println(s"address: $address2")

  /*
    Modifying JSON
   */
  val doubleQuantitics: Json => Json = root.order.items.each.quantity.int.modify(_ * 2)
  val modifiedJson = doubleQuantitics(json)
  println(s"modifiedJson: $modifiedJson")


  /*
    Recursively modifying JSON
   */
  val allStrings = Plated.transform[Json] { j =>
    j.asNumber match {
      case Some(n) => Json.fromString(n.toString)
      case None => j
    }
  }(json)

  println(s"all strings: $allStrings")
}
