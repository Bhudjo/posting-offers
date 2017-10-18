package com.buggin.postingoffers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val PriceJsonFormat: RootJsonFormat[Price] = jsonFormat2(Price)
  implicit val OfferJsonFormat: RootJsonFormat[Offer] = jsonFormat3(Offer)
  implicit val OffersJsonFormat: RootJsonFormat[Offers] = jsonFormat1(Offers)

  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed)
}
