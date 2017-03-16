package models


case class Airport(id:Long,ident:String,airType:String,name:String,iso_country:String,
                   iso_region:String,runway:List[Runway])

case class Country(id:Long,code:String,name:String,airport: List[Airport])

case class Runway(id:Long,airport_ref:Long,airport_ident:String,length_ft:String,width_ft:String,
    surface:String,le_ident:String)

case class AirportCount(country:String,airportCount:Int)

case class AirportRunways(country:String,runwayType:List[String])

case class RunwayIdent(ident:String,IdentCount:Int)

case class Reports(topAirports:List[AirportCount],leastAirport:List[AirportCount],
                   airportRunway:List[AirportRunways],commonIdent:List[RunwayIdent])