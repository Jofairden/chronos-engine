package chronos.engine.chatbot.command

import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent

class PingCommand : BotCommand() {
  override val name: String
    get() = "ping"

  override fun invoke(
    event: GenericCommandInteractionEvent,
    args: List<String>,
  ) {
    event.deferReply()
    val start = System.currentTimeMillis()

    event.reply("Pong!").queue {
      val end = System.currentTimeMillis()
      val elapsedTime = end - start
      event.hook.editOriginal("Pong: $elapsedTime ms").queue()
    }
  }
}
