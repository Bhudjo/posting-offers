package com.buggin.offers

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class OffersRouteTest
    extends WordSpec
    with Matchers
    with ScalaFutures
    with ScalatestRouteTest {

  val api: OffersRoutes = new OffersRoutes(
    system,
    system.actorOf(OfferRegistryActor.props, "OfferRegistry"))

  import api._
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
      val offer =
        OfferRequest(Price(42, "EUR"), "", MerchantID("mysticalMerchant234"))
      val marshalledOffer = Marshal(offer).to[MessageEntity].futureValue
      Post("/offers").withEntity(marshalledOffer) ~> routes ~> check {
        status shouldBe StatusCodes.Created
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===(s"""{"id":1}""")
      }
    }
    "be able to retrive all present offers" in {
      HttpRequest(uri = "/offers") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        entityAs[String] should ===(
          s"""{"Offers":[{"id":1,"price":{"amount":42,"currency":"EUR"},"description":"","merchant":{"id":"mysticalMerchant234"}}]}""")
      }
    }
//    "being able to retrieve a specific offer"
  }
}
