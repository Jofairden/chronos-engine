package chronos.engine.core.interfaces.apis

import chronos.engine.implementation.services.HTTPClientService
import io.ktor.client.statement.*

/**
 * Represents an external API.
 *
 * @property name The name of the external API.
 * @property baseUrl The base URL of the external API.
 * @property httpClient The HTTP client service used to make HTTP requests.
 */
interface IExternalApi {
    val name: String
    val baseUrl: String
    val httpClient: HTTPClientService
    /**
     * Executes an HTTP request inside the external API.
     * This method is a suspend function, allowing it to be used within a coroutine.
     *
     * @param block A lambda function that defines the behavior to be performed on the HTTP response.
     *              The lambda function is invoked with the `HttpResponse` object as the receiver,
     *              allowing direct access to the response properties and methods.
     *
     * @return The HTTP response object representing the response from the API request.
     */
    suspend fun IExternalApiRequest.execute(block: suspend HttpResponse.() -> Unit) : HttpResponse
}