package services

import javax.inject.Inject

import scala.concurrent.Future

import models.{Airport, Country, Runway}
import play.api.Logger
import play.api.cache.CacheApi
import scala.concurrent.ExecutionContext.Implicits.global
import utils.Constants._
import utils.ReaderUtility
import utils.StringUtils._

class InitializeData @Inject()(cacheApi:CacheApi,readerUtility: ReaderUtility) {
  def initializeData: Future[List[Country]] = {
    Logger.info("Initializing the data")

    val runwayData = readerUtility.loadDataFromCache("runway", "./app/resources/runways.csv")
    val airportData = readerUtility.loadDataFromCache("airport", "./app/resources/airports.csv")
    val countryData = readerUtility.loadDataFromCache("countries", "./app/resources/countries.csv")
    for {
      runwayList <- runwayData.map {
        _.tail.map { line =>
          val word = line.split(",")
          if (word.size > EIGHT) {
            Runway(word(ZERO).toLong,
              word(ONE).toLong,
              word(TWO).replaceNtrim,
              word(THREE).replaceNtrim,
              word(FOUR).replaceNtrim,
              word(FIVE).replaceNtrim,
              word(EIGHT).replaceNtrim)
          }
          else {
            Runway(word(ZERO).toLong,
              word(ONE).toLong,
              word(TWO).replaceNtrim,
              word(THREE).replaceNtrim,
              word(FOUR).replaceNtrim,
              word(FIVE).replaceNtrim,
              EMPTY_STRING.replaceNtrim)
          }
        }
      }

      aiportList <- airportData.map {
        _.tail.map { line =>
          val word = line.split(",")
          val runwayAirport = runwayList.filter(x => x.airport_ref == word(0).toLong)
          Airport(word(ZERO).toLong, word(ONE).replaceNtrim, word(TWO).replaceNtrim,
            word(THREE).replaceNtrim, word(EIGHT).replaceNtrim, word(NINE).replaceNtrim,
            runwayAirport)
        }
      }

      countryList <- countryData.map {
        _.tail.map { line =>
          val word = line.split(",")
          val countryAirport = aiportList.filter(x => x.iso_country == word(1).replaceNtrim)
          Country(word(ZERO).toLong, word(ONE).replaceNtrim, word(TWO).replaceNtrim, countryAirport)
        }
      }
    } yield countryList
  }
}
