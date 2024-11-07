package chronos.engine

import chronos.engine.chatbot.ChronosBot
import chronos.engine.core.dsl.log
import chronos.engine.infrastructure.modules.engineModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.environmentProperties
import org.koin.fileProperties
import org.koin.logger.SLF4JLogger
import java.time.OffsetDateTime
import java.time.ZoneId

/**
 * Basis applicatie
 */
class ChronosApplication {
  companion object {
    lateinit var bot: ChronosBot
    lateinit var koin: KoinApplication
  }
}

/**
 * Start de koin instantie en de Chronos bot
 */
fun main(args: Array<String>) {
  ChronosApplication.koin =
    startKoin {
      logger(SLF4JLogger())
      fileProperties()
      modules(engineModule)
      environmentProperties()
      createEagerInstances()
    }

  log("Starting bot at ${OffsetDateTime.now(ZoneId.systemDefault())}").info()
  ChronosApplication.bot = ChronosBot().start(args)
}
