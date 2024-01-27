package chronos.engine.implementation.api.coincodex

import arrow.optics.optics
import chronos.engine.core.dsl.asLoggable
import chronos.engine.core.services.SchedulerService
import chronos.engine.implementation.api.coincodex.request.GetCoinRequest
import chronos.engine.implementation.models.coincodex.Coin
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CoincodexScheduler : KoinComponent, SchedulerService(CoroutineScope(Dispatchers.IO)) {
  val client: HttpClient by inject()
  val api: CoincodexApi by inject()

  @optics
  data class ScheduledGetCoin(
    override val id: String,
    val name: String,
    override val repeatCount: Int = 0,
    override val indefinitely: Boolean = false,
    override val initialDelay: Long = 0,
    override val delayInMillis: Long = 0,
    override val task: (suspend () -> Unit)? = null,
  ) : TaskRequest(id, repeatCount, indefinitely, initialDelay, delayInMillis, task)

  fun getRequest(name: String): GetCoinRequest {
    return GetCoinRequest(name, api)
  }

  private val coinsToSchedule =
    listOf(
      ScheduledGetCoin(
        "Bitcoin",
        "BTC",
        initialDelay = 300L,
        delayInMillis = 60000L,
      ),
      ScheduledGetCoin(
        "Ethereum",
        "ETH",
        initialDelay = 300L,
        delayInMillis = 60000L,
      ),
      ScheduledGetCoin(
        "Cardano",
        "ADA",
        initialDelay = 300L,
        delayInMillis = 60000L,
      ),
    )

  fun setup() {
    runBlocking {
      asLoggable("[CoincodexScheduler] Scheduling repeated update tasks") { info() }
      scheduleRepeatingGetCoin()
      asLoggable("[CoincodexScheduler] Initialized") { info() }
    }
  }

  // TODO make a DataSource class annotation that can call
  // httpClientService.client.getJson<Coin>("https://coincodex.com/api/coincodex/get_coin/BTC")
  // automatically
  // and extract only the nodes that are deemed needed
  suspend fun scheduleRepeatingGetCoin() {
    for (coin in coinsToSchedule) {
      asLoggable("Scheduling ${coin.id} for: https://coincodex.com/api/coincodex/get_coin/") {
        info()
      }
      coin.copy(task = {
        try {
          with(api) {
            getRequest(coin.name).execute {
              val data = getJson<Coin>()
              asLoggable("${coin.id} Price High: ${data.priceHigh24Usd} - ${coin.id} Price Low: ${data.priceLow24Usd}") {
                info()
              }
            }
          }
        } catch (ex: Exception) {
          asLoggable(ex) { error() }
        }
      }).schedule()
    }
  }
}
