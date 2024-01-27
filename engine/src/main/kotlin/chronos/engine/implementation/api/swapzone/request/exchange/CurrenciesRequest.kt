package chronos.engine.implementation.api.swapzone.request.exchange

import chronos.engine.core.interfaces.apis.IExternalApiRequest
import chronos.engine.implementation.api.swapzone.SwapzoneApi

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
