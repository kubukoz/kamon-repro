package com.kubukoz.repro

import cats.effect.Effect
import com.kubukoz.repro.data.AccountRepository
import io.circe.Json
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

import scala.language.higherKinds
import cats.syntax.all._
import io.circe.syntax._
import io.circe.generic.auto._

class HelloWorldService[F[_]: Effect](repo: AccountRepository[F]) extends Http4sDsl[F] {

  val service: HttpService[F] = {
    HttpService[F] {
      case GET -> Root =>
        for {
          result <- repo.findAll
          response <- Ok(result.asJson)
        } yield response
    }
  }
}
