package chronos.engine.implementation.api.coincodex.request

import chronos.engine.core.interfaces.apis.IExternalApiRequest
import chronos.engine.implementation.api.coincodex.CoincodexApi

class GetCoinRequest(
    val coin: String,
    override val api: CoincodexApi,
) : IExternalApiRequest {
    override val endpoint: String = "get_coin/$coin"
}
