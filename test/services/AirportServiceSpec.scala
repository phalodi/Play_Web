package services

import scala.concurrent.Await

import models._
import org.scalatestplus.play.PlaySpec
import scala.concurrent.duration._

class AirportServiceSpec extends PlaySpec {

  val airportService = new AirportService
  val countryData = List(Country(1L, "aus", "australia",
    List(Airport(1L, "h1", "airType", "name", "iso", "region",
      List(Runway(1L, 1L, "ident", "len", "width", "surface", "ident"))))),
    Country(1L, "IND", "India",
      List(Airport(1L, "h1", "airType", "name", "iso", "region",
        List(Runway(1L, 1L, "ident", "len", "width", "surface", "ident"))))))


  "Get runway by country name or code" should {
    "Get country,airport and runway by country name" in {
      val result = Await.result(airportService.getRunway("india", countryData), 10 seconds)
      assert(result == List(Country(1L, "IND", "India",
        List(Airport(1L, "h1", "airType", "name", "iso", "region",
          List(Runway(1L, 1L, "ident", "len", "width", "surface", "ident")))))))
    }

    "Get country,airport and runway by country code" in {
      val result = Await.result(airportService.getRunway("ind", countryData), 10 seconds)
      assert(result == List(Country(1L, "IND", "India",
        List(Airport(1L, "h1", "airType", "name", "iso", "region",
          List(Runway(1L, 1L, "ident", "len", "width", "surface", "ident")))))))
    }
  }


  "Get Report on basic of airport" should {
    "be list highest airports" in {
      val result = Await.result(airportService.getHighestAirport(countryData), 10 seconds)
      assert(result == List(AirportCount("australia", 1), AirportCount("India", 1)))
    }

    "be list of lowest airports" in {
      val result = Await.result(airportService.getLowestAirport(countryData), 10 seconds)
      assert(result == List(AirportCount("australia", 1), AirportCount("India", 1)))
    }

    "be runway type" in {
      val result = Await.result(airportService.runwayAsAirport(countryData), 10 seconds)
      assert(result == List(AirportRunways("australia", List("surface")),
        AirportRunways("India", List("surface"))))
    }

    "be common identification for runway" in {
      val result = Await.result(airportService.commonIdentifications(countryData), 10 seconds)
      assert(result == List(RunwayIdent("ident",2)))
    }
  }
}
