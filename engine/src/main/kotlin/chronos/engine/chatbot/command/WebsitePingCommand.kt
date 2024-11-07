package chronos.engine.chatbot.command

import chronos.engine.core.chatbot.command.DeferredCommand
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import java.io.IOException
import java.net.InetAddress
import java.util.concurrent.TimeUnit

class WebsitePingCommand : DeferredCommand() {

  override val name: String
    get() = "website"

  override val description: String
    get() = "Pingt de website"

  override fun configure(data: SlashCommandData): Unit = with(data) {
    defaultPermissions = DefaultMemberPermissions.ENABLED
  }

  override suspend fun deferredInvoke(event: GenericCommandInteractionEvent) {
    val isAvailable = try {
      pingURL(
        WEBSITE_URL,
        TimeUnit.SECONDS.toMillis(5)
      )
    } catch (e: IOException) {
      event.reply("Fout bij controleren van website.").queue()
      return
    }

    event.reply("Website is ${(if (isAvailable) "online" else "offline")}").queue()
  }

  /**
   * Pings a HTTP URL. This effectively sends a HEAD request and returns `true` if the response code is in
   * the 200-399 range.
   * @param url The HTTP URL to be pinged.
   * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
   * the total timeout is effectively two times the given timeout.
   * @return `true` if the given HTTP URL has returned response code 200-399 on a HEAD request within the
   * given timeout, otherwise `false`.
   */
  fun pingURL(url: String, timeout: Long): Boolean {
    val inet = InetAddress.getByName(url)
    val reachable = inet.isReachable(timeout.toInt())
    return reachable
  }

  companion object {
    private val WEBSITE_URL = "www.marketmilker.com"
  }
}
