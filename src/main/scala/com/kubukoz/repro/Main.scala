package com.kubukoz.repro

import cats.effect.{Effect, Sync}
import com.kubukoz.repro.data.SlickAccountRepository
import com.kubukoz.repro.db.{AsyncExecutorContextAware, DefaultSlickApi}
import com.typesafe.config.{Config, ConfigFactory}
import fs2.StreamApp
import kamon.Kamon
import kamon.http4s.middleware.server.KamonSupport
import kamon.zipkin.ZipkinReporter
import monix.eval.Task
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder
import play.api.db.slick.{DbName, SlickApi}
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext
import scala.language.higherKinds
import monix.execution.Scheduler.Implicits.global

object Main extends StreamApp[Task] {

  type L[a] = fs2.Stream[Task, a]
  private val F = Sync[L]

  def stream(args: List[String],
             requestShutdown: Task[Unit]): fs2.Stream[Task, StreamApp.ExitCode] =
    for {
      _ <- F.delay(Kamon.addReporter(new ZipkinReporter()))
      stream <- ServerStream.stream[Task]
    } yield stream
}

object ServerStream {

  private val config: Config = ConfigFactory.load()
  private val slickApi: SlickApi = new DefaultSlickApi(config)

  private val dbConfig: DatabaseConfig[PostgresProfile] =
    slickApi.dbConfig[PostgresProfile](DbName("default"))

  val db: Database =
    Database.forSource(dbConfig.db.source,
                       new AsyncExecutorContextAware(AsyncExecutor.default()))

  def helloWorldService[F[_]: Effect]: HttpService[F] =
    new HelloWorldService[F](new SlickAccountRepository[F](db)).service

  def stream[F[_]: Effect](
      implicit ec: ExecutionContext): fs2.Stream[F, StreamApp.ExitCode] =
    BlazeBuilder[F]
      .bindHttp(8080, "0.0.0.0")
      .mountService(KamonSupport(helloWorldService[F]), "/")
      .serve
}
