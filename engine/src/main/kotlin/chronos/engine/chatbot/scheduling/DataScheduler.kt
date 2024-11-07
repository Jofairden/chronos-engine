package chronos.engine.chatbot.scheduling

import chronos.engine.core.dsl.log
import chronos.engine.core.scheduling.ScheduledTaskRequest
import chronos.engine.core.scheduling.SchedulerService
import chronos.engine.domain.api.mobula.MobulaService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.inject
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

  enum class DataListenType {
    BTC_PRICE;


  }

  private val coinsToSchedule =
    listOf(
      ScheduledTaskRequest(
        "Bitcoin",
        "BTC",
        initialDelay = 0L,
        delayInMillis = 60000L,
      ),
    )

  /**
   * Adds a listener to specified type
   */
  fun addListener(type: DataListenType, callback: (Double) -> Unit) {
    listeners[type]!!.add(callback)
  }

  fun DataListenType.listen(callback: (Double) -> Unit) {
    listeners[this]!!.add(callback)
  }

  suspend fun scheduleTasks() {
    for (coin in coinsToSchedule) {
      log("Scheduling ${coin.id}") {
        info()
      }
      coin.copy(task = {
        try {
          with(service) {
            val data = getMarketData(
              coin.name,
              coin.name,
              coin.name
            ).body()

            if (data.data != null) {
              delegates.btcPrice = data.data["price"] as Double
            }
          }
        } catch (ex: Exception) {
          log(ex) { error() }
        }
      }).schedule()
    }
  }


}
