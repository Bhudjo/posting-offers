package com.buggin.offers

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class OffersServiceTest
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
  }
}