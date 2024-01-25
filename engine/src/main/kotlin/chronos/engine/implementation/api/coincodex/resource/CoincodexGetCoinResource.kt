package chronos.engine.implementation.api.coincodex.resource

import io.ktor.resources.Resource

@Resource("/coin")
class CoincodexGetCoinResource {
    @Resource("{symbol}")
    class Symbol(val parent: CoincodexGetCoinResource = CoincodexGetCoinResource(), val symbol: String)
}
