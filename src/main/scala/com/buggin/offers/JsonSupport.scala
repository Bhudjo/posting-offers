package com.buggin.offers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{ DefaultJsonProtocol, RootJsonFormat }

trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val PriceJsonFormat: RootJsonFormat[Price] = jsonFormat2(Price)
  implicit val MerchantIDJsonFormat: RootJsonFormat[MerchantID] = jsonFormat1(MerchantID)
  implicit val OfferJsonFormat: RootJsonFormat[Offer] = jsonFormat4(Offer)
  implicit val OfferRequestJsonFormat: RootJsonFormat[OfferRequest] =
    jsonFormat3(OfferRequest)
  implicit val OffersJsonFormat: RootJsonFormat[Offers] = jsonFormat1(Offers)
  implicit val OfferResponseJsonFormat: RootJsonFormat[OfferResponse] =
    jsonFormat1(OfferResponse)
}
