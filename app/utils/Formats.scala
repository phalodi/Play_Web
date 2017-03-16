package utils

import models._
import play.api.libs.json._


object Formats {
  implicit val runwayFormat = Json.format[Runway]
  implicit val airportFormat = Json.format[Airport]
  implicit val countryFormat = Json.format[Country]
  implicit val airportCountFormat = Json.format[AirportCount]
  implicit val airportRunwayFormat = Json.format[AirportRunways]
  implicit val runwayIdentFormat = Json.format[RunwayIdent]
  implicit val reportsFormat = Json.format[Reports]
}
