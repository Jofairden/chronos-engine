package chronos.engine.routing

import chronos.engine.domain.api.swapzone.SwapzoneApi
import io.ktor.server.application.Application
import io.ktor.server.application.call
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

//      get<SwapzoneNetworksResource> { networks ->
//        try {
//          with(api) {
//            CurrenciesRequest(api).execute {
//              val data = getJsonCollection<NetworkObject>()
//              this@get.call.respond(data)
//            }
//          }
//        } catch (ex: Exception) {
//          asLoggable(ex) { error() }
//          call.respond(HttpStatusCode.NotFound)
//        }
//      }
    }
  }
}
