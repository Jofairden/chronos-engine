package chronos.engine.chatbot.command

import chronos.engine.core.chatbot.command.DeferredCommand
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.util.concurrent.TimeUnit

class WebsitePingCommand : DeferredCommand() {

  override val name: String
    get() = "website"

  override val description: String
    get() = "Pingt de website"

  override fun configure(data: SlashCommandData): Unit = with(data) {
    setDefaultPermissions(DefaultMemberPermissions.ENABLED)
  }

  override suspend fun deferredInvoke(event: GenericCommandInteractionEvent, args: List<String>) {
    val isAvailable = pingURL(
      WEBSITE_URL,
      TimeUnit.SECONDS.toMillis(5)
    )
    val isAvailableText =if (isAvailable) "online" else "offline"

    event.hook.editOriginal("Website is $isAvailableText").queue()
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
//    val url = url.replaceFirst("^https".toRegex(), "http") // Otherwise an exception may be thrown on invalid SSL certificates.

    try {
      val connection: HttpURLConnection = URI(url).toURL().openConnection() as HttpURLConnection
      connection.setConnectTimeout(timeout.toInt())
      connection.setReadTimeout(timeout.toInt())
      connection.setRequestMethod("GET")
      val responseCode: Int = connection.getResponseCode()
      return (responseCode in 200..399)
    } catch (exception: IOException) {
      return false
    }
  }

  companion object {
    private val WEBSITE_URL = "https://www.marketmilker.com/"
  }
}
