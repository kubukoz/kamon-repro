package com.kubukoz.repro.data

import cats.effect.LiftIO
import com.kubukoz.repro.db.SlickRepository
import slick.jdbc.PostgresProfile.api._

import scala.language.higherKinds

trait AccountRepository[F[_]] {
  val findAll: F[Seq[Account]]
}

class SlickAccountRepository[F[_]: LiftIO](val db: Database)
    extends AccountRepository[F]
    with SlickRepository[F] {
  override val findAll: F[Seq[Account]] = dbRun(Accounts.Table.result)
}
