package chronos.engine.chatbot

import chronos.engine.chatbot.command.PingCommand
import chronos.engine.core.dsl.asLoggable
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.events.onCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDA.Status
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * BotBuilder is used to build the JDA Bot application
 */
class BotBuilder : KoinComponent {
  private val jda: JDA by inject()

  fun build(): JDA {
    jda.init()
    return jda.awaitStatus(Status.INITIALIZED)
  }

  private fun getCommands(): List<SlashCommandData> {
    return listOf(
      Commands.slash("say", "Makes the bot say what you tell it to")
        .addOption(OptionType.STRING, "content", "What the bot should say", true), // Accepting a user input
      Commands.slash("ping", "Pingt de bot")
        .setDefaultPermissions(DefaultMemberPermissions.ENABLED),
    )
  }

  private fun JDA.init(): JDA {
    asLoggable("Initializing JDA") {
      info()
    }

    return initListeners().initSlashCommands()
  }

  private fun JDA.initListeners(): JDA {
    asLoggable("Initializing Listeners") {
      info()
    }
    listener<GuildReadyEvent> {
      initGuild(it)
    }
    return this
  }

  private fun initGuild(event: GuildReadyEvent) {
    asLoggable("Initializing Guild : ${event.guild.id} - ${event.guild.name}") {
      info()
    }

    event.guild.updateCommands()
      .addCommands(
        getCommands(),
      )
      .queue {
        asLoggable("Guild was initialized : ${event.guild.id} - ${event.guild.name}") {
          info()
        }
      }
  }

  private fun JDA.initSlashCommands(): JDA {
    asLoggable("Initializing commands") {
      debug()
    }

    onCommand("ping") {
      PingCommand().invoke(it, emptyList())
    }

    updateCommands().addCommands(
      getCommands(),
    ).queue {
      asLoggable("Commands initialized globally!") {
        info()
      }
    }

    return this
  }
}
