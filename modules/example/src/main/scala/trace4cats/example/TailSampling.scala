package trace4cats.example

import cats.data.NonEmptySet
import cats.effect.{IO, IOApp, Resource}
import cats.syntax.semigroup._
import fs2.Chunk
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import trace4cats._
import trace4cats.avro.AvroSpanExporter
import trace4cats.sampling.tail.cache.LocalCacheSampleDecisionStore
import trace4cats.sampling.tail.{RateTailSpanSampler, TailSamplingSpanExporter, TailSpanSampler}

import scala.concurrent.duration._

object TailSampling extends IOApp.Simple {
  override def run: IO[Unit] = Slf4jLogger.create[IO].flatMap { implicit logger: Logger[IO] =>
    (for {
      exporter <- AvroSpanExporter.udp[IO, Chunk]()

      nameSampleDecisionStore <-
        Resource.eval(LocalCacheSampleDecisionStore[IO](ttl = 10.minutes, maximumSize = Some(200000)))
      rateSampleDecisionStore <-
        Resource.eval(LocalCacheSampleDecisionStore[IO](ttl = 10.minutes, maximumSize = Some(200000)))

      probSampler = TailSpanSampler.probabilistic[IO, Chunk](probability = 0.05)
      nameSampler =
        TailSpanSampler
          .spanNameDrop[IO, Chunk](nameSampleDecisionStore, NonEmptySet.of("/healthcheck", "/readiness", "/metrics"))
      rateSampler <-
        RateTailSpanSampler.create[IO, Chunk](rateSampleDecisionStore, bucketSize = 100, tokenRate = 100.millis)

      combinedSampler =
        probSampler |+| nameSampler |+| rateSampler // TailSpanSampler.combined may also be used to combine two samplers

      samplingExporter = TailSamplingSpanExporter(exporter, combinedSampler)

      completer <- QueuedSpanCompleter[IO](TraceProcess("trace4cats"), samplingExporter, config = CompleterConfig())

      root <- Span.root[IO]("root", SpanKind.Client, SpanSampler.always, completer)
      child <- root.child("child", SpanKind.Server)
    } yield child).use(_.setStatus(SpanStatus.Internal("Error")))
  }
}
