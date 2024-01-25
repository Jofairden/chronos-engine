package chronos.engine.implementation.api.coincodex

import chronos.engine.implementation.api.ExternalApi
import com.google.gson.Gson
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.component.inject
import org.koin.core.scope.Scope

class CoincodexApi : KoinComponent, KoinScopeComponent, ExternalApi() {
    override val scope: Scope by lazy { createScope(this) }

    override val name: String = "coincodex"
    override val client: HttpClient by inject()
    override val gson: Gson by inject()
}
