package com.buggin.offers

import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class OffersRouteTest
    extends WordSpec
    with Matchers
    with ScalaFutures
    with ScalatestRouteTest
    with OffersRoutes {

  "The offer service" should {
    "not reject requests" in {
      Get("/offers") ~> routes ~> check {
        status shouldBe StatusCodes.OK
      }
    }
    "return no offers yet" in {
      Get("/offers") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        entityAs[String] should ===("""{"Offers":[]}""")
      }
    }
    "add new offers" in {
      val marshalledOffer = Marshal(Offer(1)).to[MessageEntity].futureValue
      Post("/offers").withEntity(marshalledOffer) ~> routes ~> check {
        status shouldBe StatusCodes.Created
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"id":1}""")
      }
    }
    "be able to retrive an already present offer" in {
      HttpRequest(uri = "/offers") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        entityAs[String] should ===("""{"Offers":[{"id":1}]}""")
      }
    }
  }

  override val offerRegistryActor: ActorRef =
    system.actorOf(OfferRegistryActor.props, "OfferRegistry")
}
