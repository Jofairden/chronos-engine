package chronos.engine.domain.api.coincodex

import chronos.engine.core.dsl.log
import chronos.engine.core.scheduling.SchedulerService
import chronos.engine.core.scheduling.TaskRequest
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

  private val scheduledTasks =
    listOf(
      TaskRequest(
        "Bitcoin",
        initialDelay = 300L,
        delayInMillis = 60000L,
        data = mapOf(
          "currency" to "BTC",
        )
      ),
      TaskRequest(
        "Ethereum",
        initialDelay = 300L,
        delayInMillis = 60000L,
        data = mapOf(
          "currency" to "ETH",
        )
      ),
      TaskRequest(
        "Cardano",
        initialDelay = 300L,
        delayInMillis = 60000L,
        data = mapOf(
          "currency" to "ADA",
        )
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
    for (taskRequest in scheduledTasks) {
      log("Scheduling ${taskRequest.id} for: https://coincodex.com/api/coincodex/get_coin/") {
        info()
      }
      taskRequest.copy(task = {
        try {
          with(api) {
            val currency = taskRequest.data["currency"] as String
            getRequest(currency).execute {
              val data = getJson<Coin>()
              log("${taskRequest.id} Price High: ${data.priceHigh24Usd} - ${taskRequest.id} Price Low: ${data.priceLow24Usd}") {
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
