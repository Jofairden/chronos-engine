package chronos.engine.chatbot

import chronos.engine.chatbot.command.PingCommand
import chronos.engine.chatbot.command.PriceCommand
import chronos.engine.chatbot.scheduling.DataScheduler
import chronos.engine.core.chatbot.IBot
import chronos.engine.core.dsl.log
import dev.minn.jda.ktx.events.onCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChronosBot : KoinComponent, IBot {
  private val dataScheduler: DataScheduler by inject()
  private val jda: JDA by inject()

  private val pingCommand by inject<PingCommand>()
  private val priceCommand by inject<PriceCommand>()

  /**
   * Start de bot op
   */
  fun start(block: ChronosBot.() -> Unit): ChronosBot {
    block.invoke(this)
    CoroutineScope(Dispatchers.IO).launch {
      startScheduling()
    }
    return this
  }

  /**
   * Start de schedulers
   */
  private suspend fun startScheduling() {
    dataScheduler.scheduleTasks()
    initDataListeners()
  }

  private suspend fun initDataListeners() {
    with(dataScheduler) {
      DataScheduler.DataListenType.BTC_PRICE.listen {
        updateStatus(it)
      }
    }
  }

  private fun updateStatus(price: Double) {
    log("Updating status with BTC price: ${price.toInt()}").info()
    with(jda) {
      presence.activity = Activity.customStatus(
        "BTC Prijs: $${price.toInt()}"
      );
    }
  }

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
