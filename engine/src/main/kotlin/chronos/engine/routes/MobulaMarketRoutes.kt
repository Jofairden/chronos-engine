package chronos.engine.routes

import chronos.engine.core.dsl.process
import chronos.engine.implementation.api.mobula.MobulaApi
import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureMobulaMarketRoutes() {
  val api: MobulaApi by inject()

  routing {
    route("/mobula") {
      route("/market") {
        get("/data") {
          process(api) {
            getMarketData(
              queryParam("asset")!!,
              queryParam("blockchain"),
              queryParam("symbol")
            )
          }
        }
        get("/data/{asset}/{blockchain?}/{symbol?}") {
          process(api) {
            getMarketData(
              param("asset")!!,
              param("blockchain"),
              param("symbol")
            )
          }
        }
      }
    }
  }
}
