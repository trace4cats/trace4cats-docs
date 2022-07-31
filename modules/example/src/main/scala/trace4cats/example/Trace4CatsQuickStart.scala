package trace4cats.example

import cats.Monad
import cats.data.Kleisli
import cats.effect.kernel.Resource
import cats.effect.std.Console
import cats.effect.{Async, IO, IOApp}
import cats.implicits._
import trace4cats._
import trace4cats.avro.AvroSpanCompleter

import scala.concurrent.duration._

object Trace4CatsQuickStart extends IOApp.Simple {
  def entryPoint[F[_]: Async](process: TraceProcess): Resource[F, EntryPoint[F]] =
    AvroSpanCompleter.udp[F](process, config = CompleterConfig(batchTimeout = 50.millis)).map { completer =>
      EntryPoint[F](SpanSampler.always[F], completer)
    }

  def runF[F[_]: Monad: Console: Trace]: F[Unit] =
    for {
      _ <- Trace[F].span("span1")(Console[F].println("trace this operation"))
      _ <- Trace[F].span("span2", SpanKind.Client)(Console[F].println("send some request"))
      _ <- Trace[F].span("span3", SpanKind.Client)(
        Trace[F].putAll("attribute1" -> "test", "attribute2" -> 200) >>
          Trace[F].setStatus(SpanStatus.Cancelled)
      )
    } yield ()

  def run: IO[Unit] =
    entryPoint[IO](TraceProcess("trace4cats")).use { ep =>
      ep.root("this is the root span").use { span =>
        runF[Kleisli[IO, Span[IO], *]].run(span)
      }
    }
}
