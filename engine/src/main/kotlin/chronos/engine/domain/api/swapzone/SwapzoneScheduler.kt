package chronos.engine.domain.api.swapzone

import chronos.engine.core.dsl.log
import chronos.engine.core.scheduling.SchedulerService
import chronos.engine.core.scheduling.TaskRequest
import chronos.engine.domain.api.swapzone.NetworkEntity.id
import chronos.engine.domain.api.swapzone.request.exchange.CurrenciesRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject

class SwapzoneScheduler : SchedulerService(CoroutineScope(Dispatchers.IO)) {
  val api: SwapzoneApi by inject()

  fun setup() {
    runBlocking {
      scheduleCurrencies()
      log("[SwapzoneScheduler] Initialized") { info() }
    }
  }

  fun getRequest(): CurrenciesRequest {
    return CurrenciesRequest(api)
  }

  suspend fun scheduleCurrencies() {
    TaskRequest(
      "Exchange/Currencies",
      initialDelay = 1000L,
      task = {
        with(api) {
          try {
            getRequest().execute {
              val data = getJsonCollection<NetworkObject>()
              data.forEach {
                log("Adding new NetworkEntity with id: $id").info()
              }
            }
          } catch (ex: Exception) {
            log(ex).error()
          }
        }
      }
    ).schedule()
  }
}
