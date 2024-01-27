package chronos.engine

import chronos.engine.modules.engineModule
import chronos.engine.plugins.main
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.core.context.startKoin
import org.koin.environmentProperties
import org.koin.fileProperties
import org.koin.logger.SLF4JLogger

@Suppress(
  "UtilityClassWithPublicConstructor",
  "SpreadOperator",
)
class EngineApplication {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      startKoin {
        logger(SLF4JLogger())
        fileProperties()
        modules(engineModule)
        environmentProperties()
        createEagerInstances()
      }

      embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        main()
      }.start(wait = true)
    }
  }
}

