package chronos.engine.chatbot

import chronos.engine.chatbot.command.PingCommand
import chronos.engine.chatbot.command.PriceCommand
import chronos.engine.chatbot.command.WebsitePingCommand
import chronos.engine.core.chatbot.IBotInternalProcessor
import chronos.engine.core.chatbot.command.DeferredCommand
import dev.minn.jda.ktx.events.onCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

/**
 * Manages the handling of commands within the bot
 */
class ChronosCommands : IBotInternalProcessor, KoinComponent {

  private val jda: JDA by inject()

  /**
   * Contains all commands that can be executed
   */
  private val commandStore = arrayOf(
    CommandExecution(get<PingCommand>()),
    CommandExecution(get<PriceCommand>()),
    CommandExecution(get<WebsitePingCommand>()),
  )

  /**
   * Returns the commands to be used by JDA
   */
  override fun getCommands(): List<SlashCommandData> {
    return commandStore.map(CommandExecution<out DeferredCommand>::getCommandData)
  }

  /**
   * Initializes all the commands in JDA
   */
  override fun initSlashCommands() {
    with(jda) {
      commandStore.forEach { command ->
        onCommand(command.name) {
          command.botCommand.invoke(it, emptyList())
        }
      }
    }
  }
}
