package com.buggin.offers

import akka.actor.ActorRef
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class OffersRouteTest
    extends WordSpec
    with Matchers
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
    "add new offers" ignore {
      Post("/offers") ~> routes ~> check {
        status shouldBe StatusCodes.Created
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"id":1}""")
      }
    }
  }

  override def offerRegistryActor: ActorRef =
    system.actorOf(OfferRegistryActor.props)
}
