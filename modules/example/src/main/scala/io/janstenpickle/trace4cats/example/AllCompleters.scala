package io.janstenpickle.trace4cats.example

import cats.Parallel
import cats.effect.kernel.{Async, Resource}
import cats.instances.list._
import cats.syntax.foldable._
import cats.syntax.parallel._
import io.janstenpickle.trace4cats.avro.AvroSpanCompleter
import io.janstenpickle.trace4cats.jaeger.JaegerSpanCompleter
import io.janstenpickle.trace4cats.kernel.SpanCompleter
import io.janstenpickle.trace4cats.log.LogSpanCompleter
import io.janstenpickle.trace4cats.model.TraceProcess
import io.janstenpickle.trace4cats.opentelemetry.jaeger.OpenTelemetryJaegerSpanCompleter
import io.janstenpickle.trace4cats.opentelemetry.otlp.{
  OpenTelemetryOtlpGrpcSpanCompleter,
  OpenTelemetryOtlpHttpSpanCompleter
}
import io.janstenpickle.trace4cats.stackdriver.{StackdriverGrpcSpanCompleter, StackdriverHttpSpanCompleter}
import org.http4s.blaze.client.BlazeClientBuilder
import org.typelevel.log4cats.Logger

/** This example shows how many different completers may be combined into a single completer using the provided monoid
  * instance.
  *
  * Note that both `Parallel` and `Applicative` instances of the monoid are available, if you don't provide a `Parallel`
  * typeclass then completers will be executed in sequence
  */
object AllCompleters {
  def apply[F[_]: Async: Parallel: Logger](process: TraceProcess): Resource[F, SpanCompleter[F]] =
    BlazeClientBuilder[F].resource.flatMap { blazeClient =>
      List(
        AvroSpanCompleter.udp[F](process),
        JaegerSpanCompleter[F](process),
        OpenTelemetryJaegerSpanCompleter[F](process),
        OpenTelemetryOtlpGrpcSpanCompleter[F](process),
        OpenTelemetryOtlpHttpSpanCompleter[F](blazeClient, process),
        StackdriverGrpcSpanCompleter[F](process, "gcp-project-id-123"),
        StackdriverHttpSpanCompleter[F](process, blazeClient)
      ).parSequence.map { completers =>
        (LogSpanCompleter[F](process) :: completers).combineAll
      }
    }
}
