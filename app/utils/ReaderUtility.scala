package utils

import javax.inject.Inject

import play.api.cache.CacheApi

import scala.concurrent.Future
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global


class ReaderUtility @Inject()(cacheApi: CacheApi) {

  private def read(path:String): Future[List[String]] = {
    Future(Source.fromFile(path).getLines().toList)
  }

  def loadDataFromCache(key:String,path:String):Future[List[String]] = {
    cacheApi.get[List[String]](key) match {
      case Some(x) => Future(x)
      case _ =>
        val data  = read(path)
        data map (cacheApi.set(key, _))
        data
    }
  }

}

object StringUtils {
  implicit class StringImprovements(val s: String) {
    def replaceNtrim: String = s.replace("\"", "").trim
  }
}

