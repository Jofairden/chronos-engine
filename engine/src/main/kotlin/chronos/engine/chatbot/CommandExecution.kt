package chronos.engine.chatbot

import chronos.engine.core.chatbot.command.BotCommand
import chronos.engine.core.chatbot.command.ICommand
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.koin.core.component.KoinComponent

/**
 * Defines how a command is executed and configured
 */
class CommandExecution(
  val botCommand: BotCommand,
) : KoinComponent, ICommand {

  override val name: String
    get() = botCommand.name

  override val description: String
    get() = botCommand.description

  fun getCommandData(): SlashCommandData {
    return Commands.slash(name, description).configure()
  }

  override fun invoke(event: GenericCommandInteractionEvent) {
    botCommand.invoke(event)
  }

  private fun SlashCommandData.configure(): SlashCommandData {
    botCommand.configure(this)
    return this
  }
}
