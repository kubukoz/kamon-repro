package com.kubukoz.repro.data
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

class Accounts(tag: Tag) extends Table[Account](tag, "accounts") {
  val id: Rep[AccountId] = column("id", O.AutoInc, O.PrimaryKey)
  val name: Rep[String] = column("name")

  override def * : ProvenShape[Account] =
    (id, name) <> ((Account.apply _).tupled, Account.unapply)
}

object Accounts {
  val Table = TableQuery[Accounts]
}
