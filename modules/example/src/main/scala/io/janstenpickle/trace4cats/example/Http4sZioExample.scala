package io.janstenpickle.trace4cats.example

import cats.effect.kernel.Concurrent
import cats.syntax.flatMap._
import io.janstenpickle.trace4cats.Span
import io.janstenpickle.trace4cats.base.context.Provide
import io.janstenpickle.trace4cats.example.Fs2Example.entryPoint
import io.janstenpickle.trace4cats.http4s.client.syntax._
import io.janstenpickle.trace4cats.http4s.common.Http4sRequestFilter
import io.janstenpickle.trace4cats.http4s.server.syntax._
import io.janstenpickle.trace4cats.inject.zio._
import io.janstenpickle.trace4cats.model.TraceProcess
import org.http4s.HttpRoutes
import org.http4s.client.Client
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.blaze.server.BlazeServerBuilder
import zio._
import zio.interop.catz._

object Http4sZioExample extends CatsApp {

  def makeRoutes[F[_]: Concurrent](client: Client[F]): HttpRoutes[F] = {
    object dsl extends Http4sDsl[F]
    import dsl._

    HttpRoutes.of { case req @ GET -> Root / "forward" =>
      client.expect[String](req).flatMap(Ok(_))
    }
  }

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    type F[x] = RIO[ZEnv, x]
    type G[x] = RIO[ZEnv with Has[Span[F]], x]

    implicit val spanProvide: Provide[F, G, Span[F]] = zioProvideSome
    ZIO
      .runtime[ZEnv]
      .flatMap { rt =>
        val ec = rt.platform.executor.asEC
        (for {

          ep <- entryPoint[F](TraceProcess("trace4catsHttp4s"))

          client <- BlazeClientBuilder[F](ec).resource

          routes = makeRoutes(client.liftTrace[G]()) // use implicit syntax to lift http client to the trace context

          _ <-
            BlazeServerBuilder[F](ec)
              .bindHttp(8080, "0.0.0.0")
              .withHttpApp(
                routes.inject(ep, requestFilter = Http4sRequestFilter.kubernetesPrometheus).orNotFound
              ) // use implicit syntax to inject an entry point to http routes
              .resource
        } yield ()).useForever
      }
      .exitCode
  }
}
