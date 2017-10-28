package com.buggin.offers

import akka.actor._

class OfferRegistryActor extends Actor {
  import OfferRegistryActor._

  var offers = Set.empty[Offer]

  override def receive = {
    case GetOffers =>
      sender() ! Offers(offers.toList)
    case AddOffer(offer) =>
      offers += offer
      sender() ! offer
  }
}
case object OfferRegistryActor {
  final case object GetOffers
  final case class AddOffer(offer: Offer)

  def props: Props = Props[OfferRegistryActor]
}
