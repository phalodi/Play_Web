package services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import models._
import utils.Constants._

class AirportService {

  def getRunway(country:String,allData:List[Country]): Future[List[Country]] ={
    Future(allData.filter(y=>country.equalsIgnoreCase(y.code) ||
                             country.equalsIgnoreCase(y.name) ||
                             country.toLowerCase.contains(y.name.toLowerCase)))
  }

  def getHighestAirport(allData:List[Country]): Future[List[AirportCount]] ={
    Future(allData.map(y=>AirportCount(y.name,y.airport.size)).sortWith(_.airportCount>_.airportCount).take(TEN))
  }

  def getLowestAirport(allData:List[Country]): Future[List[AirportCount]] ={
    Future(allData.map(y=>AirportCount(y.name,y.airport.size)).sortBy(_.airportCount).take(TEN))
  }

  def runwayAsAirport(allData:List[Country]): Future[List[AirportRunways]] ={
    Future(allData.map(y=>AirportRunways(y.name,y.airport.flatMap(airport=>airport.runway.map(_.surface)).distinct)))
  }
  def commonIdentifications(allData:List[Country]): Future[List[RunwayIdent]] = {
    Future(allData.flatMap(x=>x.airport.flatMap(y=>y.runway)).map(_.le_ident)
      .groupBy(x=>x).mapValues(_.size).map(x=>RunwayIdent(x._1,x._2)).toList.sortBy(_.IdentCount).takeRight(TEN))
  }

}
