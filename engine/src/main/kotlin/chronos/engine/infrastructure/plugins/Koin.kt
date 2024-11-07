package chronos.engine.infrastructure.plugins

import chronos.engine.core.dsl.asLoggable
import chronos.engine.infrastructure.modules.engineModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.context.GlobalContext
import org.koin.ktor.plugin.Koin
import org.koin.ktor.plugin.KoinApplicationStarted
import org.koin.ktor.plugin.KoinApplicationStopPreparing
import org.koin.ktor.plugin.KoinApplicationStopped
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
  // Install Koin
  if (GlobalContext.getOrNull() == null) {
    install(Koin) {
      slf4jLogger()
      modules(engineModule)
    }
  }
  // Install Ktor features
  environment.monitor.subscribe(KoinApplicationStarted) {
    asLoggable("Koin started") { info() }
  }

  environment.monitor.subscribe(KoinApplicationStopPreparing) {
    asLoggable("Koin stopping...") { info() }
  }

  environment.monitor.subscribe(KoinApplicationStopped) {
    asLoggable("Koin stopped.") { info() }
  }
}
