package com.buggin.offers

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._

import com.buggin.offers.OfferRegistryActor.GetOffers

import scala.concurrent.Future

trait OffersRoutes extends JsonSupport {

  implicit def system: ActorSystem
  def offerRegistryActor: ActorRef
  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val routes: Route =
    pathPrefix("offers") {
      get {
        val offers: Future[Offers] =
          (offerRegistryActor ? GetOffers).mapTo[Offers]
        complete(offers)
      }
    }
}
