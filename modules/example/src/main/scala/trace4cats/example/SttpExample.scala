package trace4cats.example

import cats.effect.{IO, IOApp}
import org.http4s.blaze.client.BlazeClientBuilder
import sttp.client3.http4s.Http4sBackend
import trace4cats.sttp.client3.syntax._

object SttpExample extends IOApp.Simple {
  override def run: IO[Unit] =
    (for {
      client <- BlazeClientBuilder[IO].resource
      sttpBackend = Http4sBackend.usingClient(client)
      tracedBackend = sttpBackend.liftTrace()
    } yield tracedBackend).use_
}
