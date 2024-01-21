package chronos.engine.implementation.apis.swapzone

import chronos.engine.core.interfaces.apis.IExternalApiAuthenticator
import chronos.engine.core.interfaces.apis.IExternalApiRequest
import chronos.engine.implementation.apis.ExternalApi
import chronos.engine.implementation.services.HTTPClientService
import com.google.gson.Gson
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SwapzoneApi @Autowired constructor(
    override val httpClient: HTTPClientService,
    override val gson : Gson,
    @Value("\${swapzone.api.baseurl}")
    var baseURL: String
) : ExternalApi(
    name = "Swapzone",
    baseUrl = baseURL,
    httpClient = httpClient,
    gson = gson
), IExternalApiAuthenticator {

    @Value("\${swapzone.api.key}")
    lateinit var apiKey: String

    override fun HttpRequestBuilder.authenticate(): Unit = run {
        headers {
            append("x-api-key", apiKey)
        }
    }

    override suspend fun IExternalApiRequest.execute(block: suspend HttpResponse.() -> Unit) : HttpResponse {
        return httpClient.client.get(
            api.baseUrl + endpoint
        ) {
            authenticate()
        }.let {
            it.block()
            it
        }
    }
}