package chronos.engine.chatbot.command

import chronos.engine.core.chatbot.command.DeferredCommand
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent

class PingCommand : DeferredCommand() {
  override val name: String
    get() = "ping"

  override suspend fun deferredInvoke(event: GenericCommandInteractionEvent, args: List<String>) {
    val start = System.currentTimeMillis()

    event.reply("Pong!").queue {
      val end = System.currentTimeMillis()
      val elapsedTime = end - start
      event.hook.editOriginal("Pong: $elapsedTime ms").queue()
    }
  }
}
