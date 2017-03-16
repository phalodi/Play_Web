package modules

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

import models.{Airport, Country, Runway}
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.cache.CacheApi
import play.api.mvc._
import utils.ReaderUtility
import scala.concurrent.ExecutionContext.Implicits.global

import services.InitializeData

class InitializeDataSpec extends PlaySpec with Results with Mockito {

  val mockReaderUtility=mock[ReaderUtility]
  val mockCacheApi=mock[CacheApi]
  val countryData=List(Country(1,"aus","australia",List(Airport(1,"h1","airType","name","aus","region",
    List(Runway(1,1,"ident","1","1","surface","le_ident"))))))

  "Initialize data" should {
    "load data in cache" in {
      val initializeData=new InitializeData(mockCacheApi,mockReaderUtility)
      mockReaderUtility.loadDataFromCache("runway", "./app/resources/runways.csv") returns Future(
        List(""""id","airport_ref","airport_ident","length_ft","width_ft","surface","lighted","closed","le_ident"""","""1,1,"ident",1,1,"surface",0,0,"le_ident""""))
      mockReaderUtility.loadDataFromCache("airport", "./app/resources/airports.csv") returns
      Future(List("""1,"ident","type","name","latitude_deg","longitude_deg","elevation_ft","continent","iso_country","iso_region"""",
        """1,"h1","airType","name",40.07080078125,-74.93360137939453,11,"aus","aus","region""""))
      mockReaderUtility.loadDataFromCache("countries", "./app/resources/countries.csv") returns
      Future(List("""id,code,name""","""1,"aus","australia""""))
      val result=Await.result(initializeData.initializeData,10.seconds)
      assert(result==countryData)
    }
  }


}
