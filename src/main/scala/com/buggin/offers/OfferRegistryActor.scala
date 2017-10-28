package com.buggin.offers

import akka.actor._

class OfferRegistryActor extends Actor {
  import OfferRegistryActor._

  var offers = Map.empty[Int, Offer]

  def addRequest(request: OfferRequest): Unit = {
    val offerIndex = offers.size + 1
    val offer =
      Offer(offerIndex, request.price, request.description, request.merchant)
    offers += (offerIndex -> offer)
    sender() ! OfferResponse(offer.id)
  }

  override def receive = {
    case GetOffers =>
      sender() ! Offers(offers.values.toList)
    case AddOffer(r) =>
      addRequest(r)
  }
}
case object OfferRegistryActor {
  final case object GetOffers
  final case class AddOffer(offer: OfferRequest)

  def props: Props = Props[OfferRegistryActor]
}
