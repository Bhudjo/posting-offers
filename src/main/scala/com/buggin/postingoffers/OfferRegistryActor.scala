package com.buggin.postingoffers

import akka.actor.{ Actor, ActorLogging, Props }

//#Offer-case-classes
final case class Price(amount: Int, currency: String)
final case class Offer(id: String, price: Price, description: String)
final case class Offers(Offers: Seq[Offer])
//#Offer-case-classes

object OfferRegistryActor {
  final case class ActionPerformed(description: String)
  final case object GetOffers
  final case class CreateOffer(Offer: Offer)
  final case class GetOffer(id: String)
  final case class DeleteOffer(id: String)

  def props: Props = Props[OfferRegistryActor]
}

class OfferRegistryActor extends Actor with ActorLogging {
  import OfferRegistryActor._

  var offers = Set.empty[Offer]

  def receive: Receive = {
    case GetOffers =>
      sender() ! Offers(offers.toSeq)
    case CreateOffer(offer) =>
      offers += offer
      sender() ! ActionPerformed(s"Offer ${offer.id} created.")
    case GetOffer(name) =>
      sender() ! offers.find(_.id == name)
    case DeleteOffer(id) =>
      offers.find(_.id == id) foreach { Offer => offers -= Offer }
      sender() ! ActionPerformed(s"Offer $id deleted.")
  }
}
