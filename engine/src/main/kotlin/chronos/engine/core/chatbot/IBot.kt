package chronos.engine.core.chatbot

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

interface IBot {
  fun getCommands(): List<SlashCommandData>
  fun initSlashCommands()
}
