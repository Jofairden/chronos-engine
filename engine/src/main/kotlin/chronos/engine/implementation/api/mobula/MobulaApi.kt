package chronos.engine.implementation.api.mobula

import chronos.engine.implementation.api.ExternalApi
import com.google.gson.Gson
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.component.inject
import org.koin.core.scope.Scope
import org.openapi.mobula.api.MobulaMarketApi

class MobulaApi : KoinComponent, KoinScopeComponent, ExternalApi() {
  override val scope: Scope by lazy { createScope(this) }
  override val name: String = "mobula"
  override val client: HttpClient by inject()
  override val gson: Gson by inject()
  override val coreApi: MobulaMarketApi by inject()

  suspend fun getMarketData(asset: kotlin.String, blockchain: kotlin.String?, symbol: kotlin.String?) =
    coreApi.marketDataGet(asset, blockchain, symbol)
}
