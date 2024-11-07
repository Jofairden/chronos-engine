package chronos.engine.chatbot

import chronos.engine.core.chatbot.IBotInternalProcessor
import chronos.engine.core.chatbot.command.CommandStore
import dev.minn.jda.ktx.events.onCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Manages the handling of commands within the bot
 */
class ChronosCommands : IBotInternalProcessor, KoinComponent {

  private val jda: JDA by inject()
  private val store: CommandStore by inject()

  /**
   * Returns the commands to be used by JDA
   */
  override fun getCommands(): List<SlashCommandData> {
    return store.allExecutions.map(CommandExecution::getCommandData)
  }

  /**
   * Initializes all the commands in JDA
   */
  override fun initSlashCommands() {
    with(jda) {
      store.allExecutions.forEach { command ->
        onCommand(command.name) {
          command.botCommand.invoke(it)
        }
      }
    }
  }
}
