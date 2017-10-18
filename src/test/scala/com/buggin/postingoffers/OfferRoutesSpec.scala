package com.buggin.postingoffers

//#test-top
import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

//#set-up
class OfferRoutesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest
    with OfferRoutes {
  //#test-top

  // Here we need to implement all the abstract members of OfferRoutes.
  // We use the real OfferRegistryActor to test it while we hit the Routes,
  // but we could "mock" it by implementing it in-place or by using a TestProbe() 
  override val offerRegistryActor: ActorRef =
    system.actorOf(OfferRegistryActor.props, "OfferRegistry")

  lazy val routes = OfferRoutes

  //#set-up

  //#actual-test
  "OfferRoutes" should {
    "return no Offers if no present (GET /offers)" in {
      // note that there's no need for the host part in the uri:
      val request = HttpRequest(uri = "/offers")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and no entries should be in the list:
        entityAs[String] should ===("""{"Offers":[]}""")
      }
    }
    //#actual-test

    //#testing-post
    "be able to add Offers (POST /offers)" in {
      val offer = Offer("Kapi", Price(42, "EUR"), "nice description")
      val offerEntity = Marshal(offer).to[MessageEntity].futureValue // futureValue is from ScalaFutures

      // using the RequestBuilding DSL:
      val request = Post("/offers").withEntity(offerEntity)

      request ~> routes ~> check {
        status should ===(StatusCodes.Created)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and we know what message we're expecting back:
        entityAs[String] should ===("""{"description":"Offer Kapi created."}""")
      }
    }
    //#testing-post

    "be able to remove Offers (DELETE /offers)" in {
      // Offer the RequestBuilding DSL provided by ScalatestRouteSpec:
      val request = Delete(uri = "/offers/Kapi")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and no entries should be in the list:
        entityAs[String] should ===("""{"description":"Offer Kapi deleted."}""")
      }
    }
    //#actual-test
  }
  //#actual-test

  //#set-up
}
//#set-up
