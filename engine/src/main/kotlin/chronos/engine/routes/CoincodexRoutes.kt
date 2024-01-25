package chronos.engine.routes

import chronos.engine.core.dsl.asLoggable
import chronos.engine.implementation.api.coincodex.CoincodexApi
import chronos.engine.implementation.api.coincodex.request.GetCoinRequest
import chronos.engine.implementation.api.coincodex.resource.CoincodexGetCoinResource
import chronos.engine.implementation.models.coincodex.Coin
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
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
                try {
                    with(api) {
                        GetCoinRequest(call.parameters.get("symbol")!!, api).execute {
// 							val data = getJson<Coin>()
                            try {
                                val data = gson.fromJson(bodyAsText(), Coin::class.java)
                                this@get.call.respond(data)
                            } catch (ex: Exception) {
                                this@get.call.respond(ex.toString())
                            }
                        }
                    }
                } catch (ex: Exception) {
                    asLoggable(ex) { error() }
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
