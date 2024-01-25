package chronos.engine.routes

import chronos.engine.core.dsl.asLoggable
import chronos.engine.implementation.api.swapzone.SwapzoneApi
import chronos.engine.implementation.api.swapzone.request.exchange.CurrenciesRequest
import chronos.engine.implementation.api.swapzone.resouce.SwapzoneNetworksResource
import chronos.engine.implementation.models.swapzone.NetworkObject
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

fun Application.configureSwapzoneRoutes() {
    val api: SwapzoneApi by inject()

    routing {
        route("/swapzone") {
            get {
                call.respondText { "Swapzone Routes" }
            }

            get<SwapzoneNetworksResource> { networks ->
                try {
                    with(api) {
                        CurrenciesRequest(api).execute {
                            val data = getJsonCollection<NetworkObject>()
                            this@get.call.respond(data)
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
