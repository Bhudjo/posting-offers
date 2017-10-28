package com.buggin.offers

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{ as, entity, pathPrefix }
import akka.http.scaladsl.server.{ Route, RouteConcatenation }
import akka.http.scaladsl.server.directives.MethodDirectives.{ get, post }
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.duration._
import com.buggin.offers.OfferRegistryActor.{ AddOffer, GetOffers }

import scala.concurrent.Future

trait OffersRoutes extends JsonSupport with RouteConcatenation {

  implicit lazy val timeout: Timeout = Timeout(5.seconds)
  implicit def system: ActorSystem
  def offerRegistryActor: ActorRef

  val getRoute: Route =
    pathPrefix("offers") {
      get {
        val offers: Future[Offers] =
          (offerRegistryActor ? GetOffers).mapTo[Offers]
        complete(offers)
      }
    }
  val postRoute: Route =
    pathPrefix("offers") {
      post {
        entity(as[Offer]) { offer =>
          (offerRegistryActor ? AddOffer(offer)).mapTo[Offer]
          complete((StatusCodes.Created, offer))
        }
      }
    }

  lazy val routes: Route = getRoute ~ postRoute
}
