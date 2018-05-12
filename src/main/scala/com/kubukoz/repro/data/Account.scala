package com.kubukoz.repro.data

import slick.lifted.MappedTo

case class Account(id: AccountId, name: String)
case class AccountId(value: Long) extends AnyVal with MappedTo[Long]
