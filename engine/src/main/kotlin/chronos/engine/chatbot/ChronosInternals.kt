package chronos.engine.chatbot

import chronos.engine.chatbot.scheduling.DataListenType
import chronos.engine.chatbot.scheduling.DataScheduler
import chronos.engine.core.dsl.log
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Houses all internal coupling between components for the Chronos Bot
 */
class ChronosInternals : KoinComponent {

  private val dataScheduler by inject<DataScheduler>()
  private val commands by inject<ChronosCommands>()
  private val jda by inject<JDA>()

  suspend fun start(args: Array<String>) {
    log("Initializing JDA").info()

    initJdaEventListeners()
    startScheduling()

    commands.initSlashCommands()
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
        commands.getCommands(),
      )
      .queue {
        log("Guild was initialized : ${event.guild.id}-${event.guild.name}")
          .info()
      }
  }
}
