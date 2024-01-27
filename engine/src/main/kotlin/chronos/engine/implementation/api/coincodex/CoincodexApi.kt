package chronos.engine.implementation.api.coincodex

import chronos.engine.implementation.api.ExternalApi
import com.google.gson.Gson
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.component.inject
import org.koin.core.scope.Scope
import org.openapi.coincodex.api.DefaultApi
import org.openapi.coincodex.model.CoinDTO
import org.openapitools.client.infrastructure.HttpResponse

class CoincodexApi : KoinComponent, KoinScopeComponent, ExternalApi() {
  override val scope: Scope by lazy { createScope(this) }

  override val name: String = "coincodex"
  override val client: HttpClient by inject()
  override val gson: Gson by inject()
  override val coreApi: DefaultApi by inject()

  suspend fun getCoinDetails(symbol: String): HttpResponse<CoinDTO> = coreApi.coincodexGetCoinSymbolGet(symbol)
  suspend fun getFrontpageHistory(days: Int, samples: Int, coinsLimit: Int) =
    coreApi.coincodexGetFirstpageHistoryDaysSamplesCoinsLimitGet(days, samples, coinsLimit)

  suspend fun getCoinHistory(
    symbol: String,
    startDate: java.time.LocalDate,
    endDate: java.time.LocalDate,
    samples: Int
  ) =
    coreApi.coincodexGetCoinHistorySymbolStartDateEndDateSamplesGet(symbol, startDate, endDate, samples)
}
