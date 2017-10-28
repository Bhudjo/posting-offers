package com.buggin.offers

import akka.actor.{ ActorRef, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit }
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpec, WordSpecLike }

class OfferActorsRegistrySpec
    extends TestKit(ActorSystem("testSystem"))
    with WordSpecLike
    with Matchers
    with ImplicitSender
    with BeforeAndAfterAll {

  override def afterAll {
    shutdown()
  }

  import OfferRegistryActor._
  val offerRegistryActor: ActorRef =
    system.actorOf(props, "offerRegister")

  "Asking offers should return still an empty List" in {
    offerRegistryActor ! GetOffers
    expectMsg(Offers(Seq.empty))
  }
  "Adding another offer should return me its id" in {
    val requested = Offer(1, Price(42, "EUR"), "a shopper friendly description")
    offerRegistryActor ! AddOffer(requested)
    expectMsg(requested)
    offerRegistryActor ! GetOffers
    expectMsg(Offers(Seq(requested)))
  }
}
