package chronos.engine.core.chatbot.command

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.koin.core.component.KoinComponent

abstract class BotCommand : KoinComponent, ICommand {
  abstract fun configure(data: SlashCommandData)
}
