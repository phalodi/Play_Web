package controllers

import models._
import play.api.cache.CacheApi
import services.AirportService
import scala.concurrent.{Await, Future}

import org.scalatestplus.play._
import org.specs2.mock.Mockito
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

class AirportControllerSpec extends PlaySpec with Results with Mockito {

  val airportService=mock[AirportService]
  val cacheApi=mock[CacheApi]
  val countryData=List(Country(1L,"aus","australia",
    List(Airport(1L,"h1","airType","name","iso","region",
      List(Runway(1L,1L,"ident","len","width","surface","ident"))))))
  val controller = new AirportController(airportService,cacheApi)

  "Query airports by country name and code" should {
    "return list of country with airports and runways" in {
      cacheApi.getOrElse[List[Country]]("countriesData")(Nil) returns countryData
      airportService.getRunway("aus",countryData) returns Future(countryData)
      val result: Future[Result] = controller.query("aus")(FakeRequest())
      val result1=Await.result(result,10 seconds)
      result1.header.status==200
    }

    "return error message" in {
      cacheApi.getOrElse[List[Country]]("countriesData")(Nil) returns countryData
      airportService.getRunway("aus",countryData) returns Future.failed(new Exception())
      val result: Future[Result] = controller.query("aus")(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "Something went wrong"
    }
  }

  "Querying the reports" should {
    "return reports for highst,lowest airport and runway type" in {
      cacheApi.getOrElse[List[Country]]("countriesData")(Nil) returns countryData
      airportService.getHighestAirport(countryData) returns Future(List(AirportCount("aus",1)))
      airportService.getLowestAirport(countryData) returns Future(List(AirportCount("aus",1)))
      airportService.runwayAsAirport(countryData) returns Future(List(AirportRunways("aus",List("surface"))))
      airportService.commonIdentifications(countryData) returns Future(List(RunwayIdent("aus",1)))
      val result: Future[Result] = controller.reports().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText contains "reports"
    }


    "return error message" in {
      cacheApi.getOrElse[List[Country]]("countriesData")(Nil) returns countryData
      airportService.getHighestAirport(countryData) returns Future(List(AirportCount("aus",1)))
      airportService.getLowestAirport(countryData) returns Future(List(AirportCount("aus",1)))
      airportService.runwayAsAirport(countryData) returns Future(List(AirportRunways("aus",List("surface"))))
      airportService.commonIdentifications(countryData) returns Future.failed(new Exception())
      val result: Future[Result] = controller.reports().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "Something went wrong"
    }
  }

}
