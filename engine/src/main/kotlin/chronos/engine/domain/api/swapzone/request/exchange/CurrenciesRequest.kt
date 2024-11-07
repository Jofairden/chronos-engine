package chronos.engine.domain.api.swapzone.request.exchange

import chronos.engine.core.api.IExternalApiRequest
import chronos.engine.domain.api.swapzone.SwapzoneApi

/**
 * Represents a request for retrieving currencies from the Swapzone API.
 *
 * @param api The instance of the SwapzoneApi to be used for making the request.
 */
class CurrenciesRequest(
  override val api: SwapzoneApi,
) : IExternalApiRequest {
  override val endpoint: String = "exchange/currencies"
}
