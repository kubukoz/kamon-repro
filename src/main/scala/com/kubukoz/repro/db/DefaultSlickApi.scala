package com.kubukoz.repro.db

import com.typesafe.config.Config
import play.api.db.slick.{DbName, SlickApi}
import slick.basic.{BasicProfile, DatabaseConfig}

import scala.collection.immutable

final class DefaultSlickApi(
    configuration: Config
) extends SlickApi {
  private val allConfigs =
    pureconfig.loadConfig[Map[String, Config]](configuration, "db").right.get

  lazy val allDbConfigs: Map[DbName, DatabaseConfig[BasicProfile]] =
    allConfigs.map {
      case (name, config) => (DbName(name), DefaultSlickApi.fromConfig(config))
    }

  def dbConfigs[P <: BasicProfile]()
    : immutable.Seq[(DbName, DatabaseConfig[P])] =
    allDbConfigs.toSeq.asInstanceOf[immutable.Seq[(DbName, DatabaseConfig[P])]]

  def dbConfig[P <: BasicProfile](name: DbName): DatabaseConfig[P] = {
    allDbConfigs(name).asInstanceOf[DatabaseConfig[P]]
  }
}

object DefaultSlickApi {

  def fromConfig(config: Config): DatabaseConfig[BasicProfile] = {
    DatabaseConfig.forConfig[BasicProfile](path = "", config = config)
  }
}
