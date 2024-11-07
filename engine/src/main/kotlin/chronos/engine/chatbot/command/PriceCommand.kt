package chronos.engine.chatbot.command

import chronos.engine.core.chatbot.command.DeferredCommand
import chronos.engine.domain.api.mobula.MobulaService
import kotlinx.coroutines.Dispatchers
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.koin.core.component.inject

class PriceCommand : DeferredCommand() {

  private val service: MobulaService by inject()

  override val name: String
    get() = "price"

  override val description: String
    get() = "Haalt de BTC prijs op"

  override fun configure(data: SlashCommandData): Unit = with(data) {
    setDefaultPermissions(DefaultMemberPermissions.ENABLED)
  }

  override fun coroutineContext() = Dispatchers.IO

  override suspend fun deferredInvoke(event: GenericCommandInteractionEvent, args: List<String>) {
    val data = service.getMarketData(
      "BTC",
      "BTC",
      "BTC"
    ).body()

    val price = data.data?.get("price") ?: 0f
    val priceString = String.format("%.0f", price)
    event.reply(
      "De BTC prijs is: $priceString USD"
    ).queue()
  }
}
