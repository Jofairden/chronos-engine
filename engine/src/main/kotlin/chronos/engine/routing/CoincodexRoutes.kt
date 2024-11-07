package chronos.engine.routing

import chronos.engine.core.dsl.process
import chronos.engine.domain.api.coincodex.CoincodexService
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureCoincodexNetworksRoutes() {
  val api: CoincodexService by inject()

  routing {
    route("/coincodex") {
      get {
        call.respondText { "Coincodex Routes" }
      }

      get("/coin/{symbol}") {
        process(api) {
          getCoinDetails(param("symbol")!!)
        }
      }

//      get("/coin/history/{symbol}/{startDate}/{endDate}/{samples}") {
//        process(api) {
//          val formatter = DateTimeFormatter.ISO_LOCAL_DATE
//          fun getLocalDate(value: String) = formatter.parse(
//            value
//          ) { temporal: TemporalAccessor? -> LocalDate.from(temporal) }
//          getCoinHistory(
//            param("symbol"),
//            getLocalDate( param("startDate")),
//            getLocalDate( param("endDate")),
//            param("samples").toInt()
//          )
//        }
//      }
//
//      get("/frontpage/history/{days}/{samples}/{coinsLimit}") {
//        process(api) {
//          getFrontpageHistory(param("days").toInt(), param("samples").toInt(), param("coinsLimit").toInt())
//        }
//      }
    }
  }
}
