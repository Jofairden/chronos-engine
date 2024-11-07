package chronos.engine.domain.api.coincodex

import chronos.engine.core.dsl.log
import chronos.engine.core.scheduling.ScheduledTaskRequest
import chronos.engine.core.scheduling.SchedulerService
import chronos.engine.domain.api.coincodex.request.GetCoinRequest
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject

class CoincodexScheduler : SchedulerService(CoroutineScope(Dispatchers.IO)) {
  val client: HttpClient by inject()
  val api: CoincodexService by inject()


  fun getRequest(name: String): GetCoinRequest {
    return GetCoinRequest(name, api)
  }

  private val coinsToSchedule =
    listOf(
      ScheduledTaskRequest(
        "Bitcoin",
        "BTC",
        initialDelay = 300L,
        delayInMillis = 60000L,
      ),
      ScheduledTaskRequest(
        "Ethereum",
        "ETH",
        initialDelay = 300L,
        delayInMillis = 60000L,
      ),
      ScheduledTaskRequest(
        "Cardano",
        "ADA",
        initialDelay = 300L,
        delayInMillis = 60000L,
      ),
    )

  fun setup() {
    runBlocking {
      log("[CoincodexScheduler] Scheduling repeated update tasks") { info() }
      scheduleRepeatingGetCoin()
      log("[CoincodexScheduler] Initialized") { info() }
    }
  }

  // TODO make a DataSource class annotation that can call
  // httpClientService.client.getJson<Coin>("https://coincodex.com/api/coincodex/get_coin/BTC")
  // automatically
  // and extract only the nodes that are deemed needed
  suspend fun scheduleRepeatingGetCoin() {
    for (coin in coinsToSchedule) {
      log("Scheduling ${coin.id} for: https://coincodex.com/api/coincodex/get_coin/") {
        info()
      }
      coin.copy(task = {
        try {
          with(api) {
            getRequest(coin.name).execute {
              val data = getJson<Coin>()
              log("${coin.id} Price High: ${data.priceHigh24Usd} - ${coin.id} Price Low: ${data.priceLow24Usd}") {
                info()
              }
            }
          }
        } catch (ex: Exception) {
          log(ex) { error() }
        }
      }).schedule()
    }
  }
}
