package chronos.engine.chatbot

import chronos.engine.chatbot.command.PingCommand
import chronos.engine.chatbot.command.PriceCommand
import chronos.engine.chatbot.scheduling.DataListenType
import chronos.engine.chatbot.scheduling.DataScheduler
import chronos.engine.core.chatbot.IBotInternalProcessor
import chronos.engine.core.dsl.log
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.events.onCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Houses all internal coupling between components for the Chronos Bot
 */
class ChronosInternals : KoinComponent, IBotInternalProcessor {

  private val dataScheduler by inject<DataScheduler>()
  private val jda: JDA by inject()

  private val pingCommand by inject<PingCommand>()
  private val priceCommand by inject<PriceCommand>()

  suspend fun start() {
    log("Initializing JDA").info()

    initJdaEventListeners()
    initSlashCommands()

    startScheduling()

    jda.awaitStatus(JDA.Status.INITIALIZED)
  }

  /**
   * Start the schedulers
   */
  private suspend fun startScheduling() {
    dataScheduler.scheduleTasks()
    initDataListeners()
  }

  /**
   * Initializes the data listeners
   */
  private suspend fun initDataListeners() {
    with(dataScheduler) {
      DataListenType.BTC_PRICE.listen {
        updatePresenceWithBtcPrice(it)
      }
    }
  }

  /**
   * Updates the bot's status with the BTC price
   */
  private fun updatePresenceWithBtcPrice(price: Double) {
    log("Updating status with BTC price: ${price.toInt()}").info()
    with(jda) {
      presence.activity = Activity.customStatus(
          "BTC Prijs: $${price.toInt()}"
      );
    }
  }

  private fun initJdaEventListeners() {
    log("Initializing Listeners").debug()

    with(jda) {
      listener<GuildReadyEvent> {
        initGuild(it)
      }
    }
  }

  private fun initGuild(event: GuildReadyEvent) {
    log("Initializing Guild : ${event.guild.id}-${event.guild.name}").info()

    event.guild.updateCommands()
      .addCommands(
        getCommands(),
      )
      .queue {
        log("Guild was initialized : ${event.guild.id}-${event.guild.name}")
          .info()
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
