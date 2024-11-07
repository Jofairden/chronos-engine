package chronos.engine.chatbot.scheduling

import chronos.engine.core.dsl.log
import chronos.engine.core.scheduling.SchedulerService
import chronos.engine.core.scheduling.TaskRequest
import chronos.engine.domain.api.mobula.MobulaService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class DataScheduler : SchedulerService(CoroutineScope(Dispatchers.IO)) {

  private val service: MobulaService by inject()

  /**
   * Definieert de listeners
   */
  private val listeners = mapOf(
    DataListenType.BTC_PRICE to mutableListOf<(Double) -> Unit>()
  )

  /**
   * Definieert de delegates
   */
  private val delegates = object {
    var btcPrice by Delegates.observable(0.0) { _, _, newValue ->
      listeners[DataListenType.BTC_PRICE]!!.forEach { it.invoke(newValue) }
    }
  }

  private val scheduledTasks =
    listOf(
      TaskRequest(
        "datascheduler-btc-price",
        indefinitely = true,
        initialDelay = 0L,
        delayInMillis = TimeUnit.MINUTES.toMillis(5),
        data = mapOf(
          "asset" to "btc",
          "blockchain" to "btc",
          "symbol" to "btc"
        ),
        task = {
          syncBtcPrice("btc", "btc", "btc")
        }
      ),
    )

  /**
   * Adds a listener to specified type
   */
  fun addListener(type: DataListenType, callback: (Double) -> Unit) {
    listeners[type]!!.add(callback)
  }

  /**
   * Adds a listener to the type
   */
  fun DataListenType.listen(callback: (Double) -> Unit) {
    listeners[this]!!.add(callback)
  }

  private suspend fun syncBtcPrice(asset: String, blockchain: String, symbol: String) {
    try {
      with(service) {
        val data = getMarketData(asset, blockchain, symbol).body()

        if (data.data != null) {
          delegates.btcPrice = data.data["price"] as Double
        }
      }
    } catch (ex: Exception) {
      log(ex) { error() }
    }
  }

  suspend fun scheduleTasks() {
    for (taskRequest in scheduledTasks) {
      log("Scheduling ${taskRequest.id}").info()
      taskRequest.schedule()
    }
  }
}
