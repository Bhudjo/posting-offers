package com.buggin.offers

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import akka.pattern.ask
import akka.util.Timeout
import com.buggin.offers.OfferRegistryActor.{AddOffer, GetOffers}

import scala.concurrent.Future
import scala.concurrent.duration._

class OffersRoutes(system: ActorSystem, offerRegistryActor: ActorRef)
    extends JsonSupport
    with RouteConcatenation {

  import system.log

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

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
          val future = (offerRegistryActor ? AddOffer(offer)).mapTo[Offer]
          onSuccess(future) { f =>
            log.info(s"POST successful. Created resource ${}", f.id)
            complete((StatusCodes.Created, f))
          }
        }
      }
    }

  lazy val routes: Route = getRoute ~ postRoute
}
