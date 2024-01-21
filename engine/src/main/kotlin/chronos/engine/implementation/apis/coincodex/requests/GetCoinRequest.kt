package chronos.engine.implementation.apis.coincodex.requests

import chronos.engine.core.interfaces.apis.IExternalApiRequest
import chronos.engine.implementation.apis.coincodex.CoincodexApi

class GetCoinRequest(
    val coin: String,
    override val api: CoincodexApi,
) : IExternalApiRequest {
    override val endpoint: String = "get_coin/${coin}"
}