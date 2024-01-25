package chronos.engine.plugins

import chronos.engine.routes.configureCoincodexNetworksRoutes
import chronos.engine.routes.configureSwapzoneRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Routing

fun Application.configureRouting() {
    install(Routing)

    configureCoincodexNetworksRoutes()
    configureSwapzoneRoutes()
}
