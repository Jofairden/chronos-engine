package chronos.engine

import chronos.engine.chatbot.BotBuilder
import chronos.engine.chatbot.ChronosBot
import chronos.engine.infrastructure.modules.engineModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.environmentProperties
import org.koin.fileProperties
import org.koin.logger.SLF4JLogger

class ChronosApplication {
  companion object {
    lateinit var bot: ChronosBot
    lateinit var koin: KoinApplication
  }
}

fun main(args: Array<String>) {
  ChronosApplication.koin =
    startKoin {
      logger(SLF4JLogger())
      fileProperties()
      modules(engineModule)
      environmentProperties()
      createEagerInstances()
    }

  ChronosApplication.bot = BotBuilder().build(args)
}

fun koin(): KoinApplication {
  return ChronosApplication.koin
}
