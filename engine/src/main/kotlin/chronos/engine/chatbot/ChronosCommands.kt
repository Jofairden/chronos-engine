package chronos.engine.chatbot

import chronos.engine.chatbot.command.PingCommand
import chronos.engine.chatbot.command.PriceCommand
import chronos.engine.core.chatbot.IBotInternalProcessor
import dev.minn.jda.ktx.events.onCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChronosCommands : IBotInternalProcessor, KoinComponent {
  private val jda: JDA by inject()
  private val pingCommand by inject<PingCommand>()
  private val priceCommand by inject<PriceCommand>()

  override fun getCommands(): List<SlashCommandData> {
    return listOf(
      Commands.slash("say", "Makes the bot say what you tell it to")
        .addOption(OptionType.STRING, "content", "What the bot should say", true), // Accepting a user input
      Commands.slash("ping", "Pingt de bot")
        .setDefaultPermissions(DefaultMemberPermissions.ENABLED),
      Commands.slash("price", "Haalt de BTC prijs op")
        .setDefaultPermissions(DefaultMemberPermissions.ENABLED),
    )
  }

  override fun initSlashCommands() {
    with(jda) {
      onCommand("ping") {
        pingCommand.invoke(it, emptyList());
      }
      onCommand("price") {
        priceCommand.invoke(it, emptyList())
      }
    }
  }

}
