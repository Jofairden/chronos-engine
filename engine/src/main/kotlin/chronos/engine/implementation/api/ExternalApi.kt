package chronos.engine.implementation.api

import chronos.engine.core.interfaces.apis.IExternalApi
import chronos.engine.core.interfaces.apis.IExternalApiRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import java.lang.reflect.Type

/**
 * Represents an abstract class for an external API.
 */
abstract class ExternalApi : IExternalApi {
    abstract override val name: String
    abstract override val gson: Gson
    abstract override val client: HttpClient

    /**
     * Retrieves a JSON response and parses it to the specified type using a TypeToken.
     *
     * @param T The type of object to parse the JSON response into.
     * @param block A lambda function that returns the TypeToken representing the type to parse.
     * @return The parsed JSON object of type T.
     */
    suspend inline fun <reified T> HttpResponse.getJsonWithTypeToken(block: () -> Type): T {
        val typeToken = block()
        val json = gson.fromJson<T>(bodyAsText(), typeToken)
        return json
    }

    /**
     * Retrieves a JSON response and parses it to a collection of objects of type T.
     *
     * @param T The type of objects in the collection.
     * @return The parsed JSON collection of objects of type T.
     */
    suspend inline fun <reified T> HttpResponse.getJsonCollection() =
        getJsonWithTypeToken<Collection<T>> {
            object : TypeToken<Collection<T>>() {}.type
        }

    /**
     * Retrieves a JSON response and parses it into the specified type.
     *
     * @return The parsed JSON object of type T.
     */
    suspend inline fun <reified T : Any> HttpResponse.getJson(): T = gson.fromJson(bodyAsText(), T::class.java)

    /**
     * Executes an HTTP request inside the external API.
     *
     * @param block A lambda function that defines the behavior to be performed on the HTTP response.
     *              The lambda function is invoked with the `HttpResponse` object as the receiver,
     *              allowing direct access to the response properties and methods.
     *
     * @return The HTTP response object representing the response from the API request.
     */
    override suspend fun IExternalApiRequest.execute(block: suspend HttpResponse.() -> Unit): HttpResponse {
        return client.get(endpoint).let {
            it.block()
            it
        }
    }
}
