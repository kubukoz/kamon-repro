package com.kubukoz.repro.db

import cats.effect.{IO, LiftIO}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.language.higherKinds

trait SlickRepository[F[_]] {
  def db: Database

  def dbRun[R](action: DBIOAction[R, NoStream, Nothing])(
      implicit F: LiftIO[F]): F[R] =
    Utils.deferFuture(db.run(action))
}

object Utils {
  //https://github.com/typelevel/cats-effect/issues/199
  def deferFuture[F[_]: LiftIO, A](f: => Future[A]): F[A] =
    LiftIO[F].liftIO(IO.fromFuture(IO(f)))
}
