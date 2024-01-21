package chronos.engine.implementation.apis.swapzone

import chronos.engine.core.dsl.asLoggable
import chronos.engine.core.services.SchedulerService
import chronos.engine.implementation.apis.swapzone.requests.exchange.CurrenciesRequest
import chronos.engine.implementation.models.swapzone.NetworkObject
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SwapzoneScheduler
    @Autowired
    constructor(
        val api: SwapzoneApi,
        val swapZone: SwapzoneService,
    ) : SchedulerService(CoroutineScope(Dispatchers.IO)) {
        @PostConstruct()
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
                try {
                    asLoggable(api.baseUrl) {
                        error()
                    }
                    with(api) {
                        getRequest().execute {
                            val data = getJsonCollection<NetworkObject>()
                            data.forEach {
                                val id =
                                    swapZone.create(
                                        SwapzoneService.NetworkCreateRequest(
                                            it.name,
                                            it.ticker,
                                            it.network,
                                            it.smartContract,
                                        ),
                                    )
                                asLoggable("Adding new NetworkEntity with id: $id") {
                                    info()
                                }
                            }
                        }
                    }
                } catch (ex: Exception) {
                    asLoggable(ex) { error() }
                }
            }.schedule()
        }
    }
