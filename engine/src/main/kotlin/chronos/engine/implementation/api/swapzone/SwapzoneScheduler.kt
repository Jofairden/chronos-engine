package chronos.engine.implementation.api.swapzone

import chronos.engine.core.dsl.asLoggable
import chronos.engine.core.services.SchedulerService
import chronos.engine.implementation.api.swapzone.request.exchange.CurrenciesRequest
import chronos.engine.implementation.models.swapzone.NetworkObject
import chronos.engine.implementation.models.swapzone.db.NetworkEntity.id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SwapzoneScheduler : KoinComponent, SchedulerService(CoroutineScope(Dispatchers.IO)) {
    val api: SwapzoneApi by inject()

    fun setup() {
        runBlocking {
            scheduleCurrencies()
            asLoggable("[SwapzoneScheduler] Initialized") { info() }
        }
    }

    fun getRequest(): CurrenciesRequest {
        return CurrenciesRequest(api)
    }

    suspend fun scheduleCurrencies() {
        TaskRequest(
            "Exchange/Currencies",
            initialDelay = 1000L,
        ) {
            with(api) {
                try {
                    getRequest().execute {
                        val data = getJsonCollection<NetworkObject>()
                        data.forEach {
                            asLoggable("Adding new NetworkEntity with id: $id") {
                                info()
                            }
                        }
                    }
                } catch (ex: Exception) {
                    asLoggable(ex) { error() }
                }
            }
        }.schedule()
    }
}
