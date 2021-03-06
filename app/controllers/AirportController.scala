package controllers

import javax.inject._

import models.{Reports, Country}
import play.api.cache.CacheApi
import play.api.mvc._
import services.AirportService
import scala.concurrent.ExecutionContext.Implicits.global



/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's airport page.
 */
@Singleton
class AirportController @Inject()(airportService:AirportService,cacheApi:CacheApi) extends Controller {


  def home:Action[AnyContent] = Action {
    Ok(views.html.home())
  }


  def query(name:String):Action[AnyContent] = Action.async {
    val allData=cacheApi.getOrElse[List[Country]]("countriesData")(Nil)
    val filterAirport = airportService.getRunway(name,allData)

    filterAirport.map(countries=>Ok(views.html.result(countries))).recover{
      case ex:Exception=>
        InternalServerError("Something went wrong")
    }

  }

  def reports():Action[AnyContent] = Action.async{
    val allData=cacheApi.getOrElse[List[Country]]("countriesData")(Nil)
    val topAirports=airportService.getHighestAirport(allData)
    val lowAirport=airportService.getLowestAirport(allData)
    val airportRunway=airportService.runwayAsAirport(allData)
    val commonIdent=airportService.commonIdentifications(allData)
    val reports=for{
      top<-topAirports
      low<-lowAirport
      runway<-airportRunway
      commonIdentification<-commonIdent
    } yield Reports(top,low,runway,commonIdentification)
    reports.map(report=>Ok(views.html.reports(report))).recover{
      case ex:Throwable=>
        InternalServerError("Something went wrong")
    }
  }
}

