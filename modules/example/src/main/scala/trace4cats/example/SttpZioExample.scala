package trace4cats.example

import _root_.sttp.client3.http4s.Http4sBackend
import org.http4s.blaze.client.BlazeClientBuilder
import trace4cats._
import trace4cats.context.Provide
import trace4cats.context.zio.instances._
import trace4cats.sttp.client3.syntax._
import zio._
import zio.interop.catz._

object SttpZioExample extends CatsApp {

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    type F[x] = RIO[ZEnv, x]
    type G[x] = RIO[ZEnv with Has[Span[F]], x]
    implicit val spanProvide: Provide[F, G, Span[F]] = zioProvideSome

    (for {
      client <- BlazeClientBuilder[F].resource
      sttpBackend = Http4sBackend.usingClient(client)
      tracedBackend = sttpBackend.liftTrace[G]()
    } yield tracedBackend).use_.exitCode
  }
}
