package chronos.engine.plugins

import chronos.engine.routes.configureCoincodexNetworksRoutes
import chronos.engine.routes.configureMobulaMarketRoutes
import chronos.engine.routes.configureSwapzoneRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Routing

fun Application.installRouting() {
  install(Routing)

  configureCoincodexNetworksRoutes()
  configureSwapzoneRoutes()
  configureMobulaMarketRoutes()
}
