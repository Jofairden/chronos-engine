package chronos.engine.chatbot

import chronos.engine.core.dsl.log
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDA.Status
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * BotBuilder is used to build the JDA Bot application
 */
class BotBuilder : KoinComponent {
  private val jda: JDA by inject()

  private lateinit var bot: ChronosBot

  fun build(args: Array<String>): ChronosBot {
    bot = ChronosBot()

    return bot.start {
      log("Initializing JDA").info()
      initListeners()
      initSlashCommands()
      jda.awaitStatus(Status.INITIALIZED)
    }
  }

  private fun initListeners() {
    log("Initializing Listeners").debug()

    with(jda) {
      listener<GuildReadyEvent> {
        initGuild(it)
      }
    }
  }

  private fun initGuild(event: GuildReadyEvent) {
    log("Initializing Guild : ${event.guild.id}-${event.guild.name}")
      .info()

    event.guild.updateCommands()
      .addCommands(
        bot.getCommands(),
      )
      .queue {
        log("Guild was initialized : ${event.guild.id}-${event.guild.name}")
          .info()
      }
  }
}
