package com.buggin.offers

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete

trait OffersRoutes {

  lazy val routes: Route =
    pathPrefix("offers") {
      get {
        complete(StatusCodes.OK)
      }
    }
}
