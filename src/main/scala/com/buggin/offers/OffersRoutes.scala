package com.buggin.offers

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import akka.pattern.ask
import akka.util.Timeout
import com.buggin.offers.OfferRegistryActor.{AddOffer, GetOffers}

import scala.concurrent.Future

class OffersRoutes(system: ActorSystem, offerRegistryActor: ActorRef)
    extends JsonSupport
    with RouteConcatenation {

  implicit lazy val timeout: Timeout = Timeout(t, TimeUnit.SECONDS)
  val log: LoggingAdapter = system.log

  lazy val routes: Route = getRoute ~ postRoute

  private val t = system.settings.config.getLong("api.request-timeout")
  private val getRoute: Route =
    pathPrefix("offers") {
      get {
        val offers: Future[Offers] =
          (offerRegistryActor ? GetOffers).mapTo[Offers]
        complete(offers)
      }
    }
  private val postRoute: Route =
    pathPrefix("offers") {
      post {
        entity(as[OfferRequest]) { offer =>
          val future =
            (offerRegistryActor ? AddOffer(offer)).mapTo[OfferResponse]
          onSuccess(future) { f =>
            log.info("POST successful. Created resource with id #{}", f.id)
            complete((StatusCodes.Created, f))
          }
        }
      }
    }
}
