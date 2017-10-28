package com.buggin.offers

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec, WordSpecLike}

class OfferRegistrySpec
    extends TestKit(ActorSystem("testSystem"))
    with WordSpecLike
    with Matchers
    with ImplicitSender
    with BeforeAndAfterAll {

  override def afterAll {
    shutdown()
  }

  "Asking offers should return still an empty List" in {
    import OfferRegistryActor._
    val offerRegistryActor: ActorRef =
      system.actorOf(props, "offerRegister")
    offerRegistryActor ! GetOffers
    expectMsg(Offers(Seq.empty))
  }
}
