package chronos.engine.chatbot.command

import chronos.engine.core.chatbot.command.DeferredCommand
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

class PingCommand : DeferredCommand() {

  override val name: String
    get() = "ping"

  override val description: String
    get() = "Pingt de bot"

  override fun configure(data: SlashCommandData): Unit = with(data) {
//    addOption(OptionType.STRING, "content", "What the bot should say", true)
    defaultPermissions = DefaultMemberPermissions.ENABLED
  }

  override suspend fun deferredInvoke(event: GenericCommandInteractionEvent) {
    val start = System.currentTimeMillis()

    event.reply("Pong!").queue {
      val end = System.currentTimeMillis()
      val elapsedTime = end - start
      event.hook.editOriginal("Pong: $elapsedTime ms").queue()
    }
  }
}
