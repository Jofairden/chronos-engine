package chronos.engine.routes

import chronos.engine.implementation.api.coincodex.CoincodexApi
import chronos.engine.implementation.api.coincodex.resource.CoincodexGetCoinResource
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureCoincodexNetworksRoutes() {
    val api: CoincodexApi by inject()

    routing {
        route("/coincodex") {
            get {
                call.respondText { "Coincodex Routes" }
            }

            get<CoincodexGetCoinResource.Symbol> { coin ->
                val symbol = call.parameters["symbol"]!!
                with (api) {
                    with(api.getCoinDetails(symbol)) {
                        handle(this@get)
                    }
                }
            }
        }
    }
}
