package chronos.engine.core.chatbot.command

import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent

interface ICommand {
  val name: String
  val description: String

  fun invoke(
    event: GenericCommandInteractionEvent,
  )
}
