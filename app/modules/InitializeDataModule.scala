package modules

import javax.inject.Inject

import play.api.cache.CacheApi
import services.InitializeData
import scala.concurrent.ExecutionContext.Implicits.global

class InitializeDataModule @Inject()(initialzeData:InitializeData,cacheApi:CacheApi) {
  initialzeData.initializeData.map(cacheApi.set("countriesData",_)).recover{
    case ex:Exception=>cacheApi.set("countriesData", Nil)
  }
}

