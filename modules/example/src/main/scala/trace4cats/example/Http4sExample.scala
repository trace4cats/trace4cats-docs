package trace4cats.example

import cats.data.Kleisli
import cats.effect.{Concurrent, IO, Resource, ResourceApp}
import cats.syntax.flatMap._
import org.http4s.HttpRoutes
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import trace4cats._
import trace4cats.example.Fs2Example.entryPoint
import trace4cats.http4s.client.syntax._
import trace4cats.http4s.common.Http4sRequestFilter
import trace4cats.http4s.server.syntax._

object Http4sExample extends ResourceApp.Forever {

  def makeRoutes[F[_]: Concurrent](client: Client[F]): HttpRoutes[F] = {
    object dsl extends Http4sDsl[F]
    import dsl._

    HttpRoutes.of { case req @ GET -> Root / "forward" =>
      client.expect[String](req).flatMap(Ok(_))
    }
  }

  override def run(args: List[String]): Resource[IO, Unit] = {
    type F[x] = IO[x]
    type G[x] = Kleisli[F, Span[F], x]

    for {
      ep <- entryPoint[F](TraceProcess("trace4catsHttp4s"))
      client <- BlazeClientBuilder[F].resource

      routes = makeRoutes[G](client.liftTrace()) // use implicit syntax to lift http client to the trace context

      _ <-
        BlazeServerBuilder[F]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(
            routes.inject(ep, requestFilter = Http4sRequestFilter.kubernetesPrometheus).orNotFound
          ) // use implicit syntax to inject an entry point to http routes
          .resource
    } yield ()
  }
}
