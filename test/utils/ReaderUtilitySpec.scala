package utils

import scala.concurrent.{Await, Future}

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.cache.CacheApi
import scala.concurrent.duration._
import utils.StringUtils._


class ReaderUtilitySpec extends PlaySpec with  MockitoSugar {

  val mockCache=mock[CacheApi]
  val readerUtility=new ReaderUtility(mockCache)
  "read" should {
    "should return list of string" in {
       val result=Await.result(readerUtility.loadDataFromCache("runway", "./app/resources/runways.csv"),10 seconds)
      assert(result.nonEmpty)

    }
  }

  "string should be" should {
    "replace and trim" in {
      val result=""""aus """.replaceNtrim
      assert(result=="aus")

    }
  }
}
