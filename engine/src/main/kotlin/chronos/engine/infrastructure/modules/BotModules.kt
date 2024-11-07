package chronos.engine.infrastructure.modules

import chronos.engine.chatbot.ChronosCommands
import chronos.engine.chatbot.ChronosInternals
import chronos.engine.chatbot.command.PingCommand
import chronos.engine.chatbot.command.PriceCommand
import chronos.engine.chatbot.command.WebsitePingCommand
import chronos.engine.chatbot.scheduling.DataScheduler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val commandModules = module {
  factory<PingCommand> {
    PingCommand()
  }
  factory<PriceCommand> {
    PriceCommand()
  }
  factory<WebsitePingCommand> {
    WebsitePingCommand()
  }
  singleOf(::ChronosCommands)
}

val botModules = module {
  singleOf(::ChronosInternals)
  singleOf(::DataScheduler)
  includes(commandModules)
}
