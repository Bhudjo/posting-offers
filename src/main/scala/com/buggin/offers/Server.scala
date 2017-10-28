package com.buggin.offers

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

object Server extends App {
  implicit val system: ActorSystem = ActorSystem("simpleHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContext = system.dispatcher

  val offerRegistryActor: ActorRef =
    system.actorOf(OfferRegistryActor.props, "OfferRegistryActor")

  val api = new OffersRoutes(system, offerRegistryActor)
  import api._
  val serverBindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

  StdIn.readLine()

  serverBindingFuture
    .flatMap(_.unbind())
    .onComplete { done =>
      done.failed.map { ex =>
        log.error(ex, "Failed unbinding")
      }
      system.terminate()
    }
}
