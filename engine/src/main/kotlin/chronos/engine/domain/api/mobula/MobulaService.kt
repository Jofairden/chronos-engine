package chronos.engine.domain.api.mobula

import chronos.engine.domain.api.ExternalApi
import com.google.gson.Gson
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.component.inject
import org.koin.core.scope.Scope
import org.openapi.Mobula.api.MobulaApi

class MobulaService : KoinComponent, KoinScopeComponent, ExternalApi() {
  override val scope: Scope by lazy { createScope(this) }
  override val name: String = "mobula"
  override val client: HttpClient by inject()
  override val gson: Gson by inject()
  override val coreApi: MobulaApi by inject()

  suspend fun getMarketData(
    asset: String,
    blockchain: String?,
    symbol: String?,
  ) = coreApi.fetchAssetMarketData(asset, blockchain, symbol)
}
