package chronos.engine

import chronos.engine.chatbot.BotBuilder
import chronos.engine.infrastructure.modules.engineModule
import net.dv8tion.jda.api.JDA
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.environmentProperties
import org.koin.fileProperties
import org.koin.logger.SLF4JLogger

@Suppress(
  "UtilityClassWithPublicConstructor",
  "SpreadOperator",
)
class ChronosApplication {
  companion object {
    lateinit var jda: JDA
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

  ChronosApplication.jda = BotBuilder().build()
}

fun jda(): JDA {
  return ChronosApplication.jda
}

fun koin(): KoinApplication {
  return ChronosApplication.koin
}
