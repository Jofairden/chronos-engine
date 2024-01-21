package chronos.engine.implementation.apis.coincodex

import chronos.engine.core.interfaces.apis.IExternalApiRequest
import chronos.engine.implementation.apis.ExternalApi
import chronos.engine.implementation.services.HTTPClientService
import com.google.gson.Gson
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CoincodexApi @Autowired constructor(
    override val httpClient: HTTPClientService,
    override val gson : Gson
) : ExternalApi(
    name = "coincodex",
    baseUrl ="https://coincodex.com/api/coincodex/",
    httpClient = httpClient,
    gson = gson
) {
    override suspend fun IExternalApiRequest.execute(block: suspend HttpResponse.() -> Unit) : HttpResponse {
        return httpClient.client.get(
            api.baseUrl + endpoint
        ).let {
            it.block()
            it
        }
    }
}