package chronos.engine.infrastructure.plugins

import chronos.engine.routing.configureCoincodexNetworksRoutes
import chronos.engine.routing.configureMobulaMarketRoutes
import chronos.engine.routing.configureSwapzoneRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Routing

fun Application.installRouting() {
  install(Routing)

  configureCoincodexNetworksRoutes()
  configureSwapzoneRoutes()
  configureMobulaMarketRoutes()
}
