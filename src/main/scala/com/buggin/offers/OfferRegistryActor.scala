package com.buggin.offers

import akka.actor._

class OfferRegistryActor extends Actor {
  import OfferRegistryActor._

  var offers: Seq[Nothing] = Seq.empty

  override def receive = {
    case GetOffers(receiver) =>
      receiver ! offers
  }
}
case object OfferRegistryActor {
  final case class GetOffers(receiver: ActorRef)

  def props: Props = Props[OfferRegistryActor]
}
