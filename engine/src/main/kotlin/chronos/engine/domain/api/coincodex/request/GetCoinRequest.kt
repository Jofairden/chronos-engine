package chronos.engine.domain.api.coincodex.request

import chronos.engine.core.api.IExternalApiRequest
import chronos.engine.domain.api.coincodex.CoincodexService

class GetCoinRequest(
  val coin: String,
  override val api: CoincodexService,
) : IExternalApiRequest {
  override val endpoint: String = "get_coin/$coin"
}
