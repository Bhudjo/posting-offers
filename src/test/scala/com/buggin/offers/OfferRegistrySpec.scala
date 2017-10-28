package com.buggin.offers

import akka.actor.{ ActorRef, ActorSystem }
import akka.testkit.TestKit
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpec, WordSpecLike }

class OfferRegistrySpec
    extends TestKit(ActorSystem("testSystem"))
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  override def afterAll {
    shutdown()
  }

  "Asking offers should return an empty List yet" in {
    import OfferRegistryActor._
    val offerRegistryActor: ActorRef =
      system.actorOf(props, "offerRegister")
    offerRegistryActor ! GetOffers(testActor)
    expectMsg(Seq.empty)
  }
}
