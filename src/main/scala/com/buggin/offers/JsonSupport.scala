package com.buggin.offers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val OfferJsonFormat: RootJsonFormat[Offer] = jsonFormat1(Offer)
  implicit val OffersJsonFormat: RootJsonFormat[Offers] = jsonFormat1(Offers)
}
