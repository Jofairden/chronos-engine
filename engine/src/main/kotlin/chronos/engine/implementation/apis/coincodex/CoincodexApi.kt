package chronos.engine.implementation.apis.coincodex

import chronos.engine.core.interfaces.apis.IExternalApiRequest
import chronos.engine.implementation.apis.ExternalApi
import chronos.engine.implementation.services.HTTPClientService
import com.google.gson.Gson
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class CoincodexApi @Autowired constructor(
    override val httpClient: HTTPClientService,
    override val gson : Gson,
    @Value("\${coincodex.api.baseurl}")
    override val baseUrl: String,
) : ExternalApi(
    name = "coincodex",
    baseUrl = baseUrl,
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