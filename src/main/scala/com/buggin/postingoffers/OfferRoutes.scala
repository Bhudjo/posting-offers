package com.buggin.postingoffers

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{delete, get, post}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._

//#Offer-routes-class
trait OfferRoutes extends JsonSupport {
  //#Offer-routes-class

  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[OfferRoutes])

  // other dependencies that OfferRoutes use
  def offerRegistryActor: ActorRef

  // Required by the `ask` (?) method below
  implicit lazy val timeout: Timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  //#all-routes
  //#Offers-get-post
  //#Offers-get-delete
  lazy val OfferRoutes: Route =
    pathPrefix("offers") {
      concat(
        //#Offers-get-delete
        pathEnd {
          concat(
            get {
              val Offers: Future[Offers] =
                (offerRegistryActor ? GetOffers).mapTo[Offers]
              complete(Offers)
            },
            post {
              entity(as[Offer]) { offer =>
                val offerCreated: Future[ActionPerformed] =
                  (offerRegistryActor ? CreateOffer(offer)).mapTo[ActionPerformed]
                onSuccess(offerCreated) { performed =>
                  log.info(s"Created offer [${offer.id}]: ${performed.description}")
                  complete((StatusCodes.Created, performed))
                }
              }
            }
          )
        },
        //#Offers-get-post
        //#Offers-get-delete
        path(Segment) { id =>
          concat(
            get {
              //#retrieve-Offer-info
              val maybeOffer: Future[Option[Offer]] =
                (offerRegistryActor ? GetOffer(id)).mapTo[Option[Offer]]
              rejectEmptyResponse {
                complete(maybeOffer)
              }
              //#retrieve-Offer-info
            },
            delete {
              //#Offers-delete-logic
              val OfferDeleted: Future[ActionPerformed] =
                (offerRegistryActor ? DeleteOffer(id)).mapTo[ActionPerformed]
              onSuccess(OfferDeleted) { performed =>
                log.info("Deleted offer [{}]: {}", id, performed.description)
                complete((StatusCodes.OK, performed))
              }
              //#Offers-delete-logic
            }
          )
        }
      )
      //#Offers-get-delete
    }
  //#all-routes
}
