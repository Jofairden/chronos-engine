package chronos.engine.implementation.apis

import chronos.engine.core.interfaces.apis.IExternalApi
import chronos.engine.implementation.services.HTTPClientService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.statement.*
import java.lang.reflect.Type

/**
 * Represents an abstract External API.
 *
 * @property name The name of the External API.
 * @property baseUrl The base URL of the External API.
 * @property httpClient The HTTP client service used to make HTTP requests.
 * @property gson The Gson instance used for JSON parsing.
 */
abstract class ExternalApi(
    override val name: String,
    override val baseUrl: String,
    override val httpClient: HTTPClientService,
    open val gson: Gson
) : IExternalApi {

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
        getJsonWithTypeToken<Collection<T>>() {
            object : TypeToken<Collection<T>>() {}.type
        }

    /**
     * Retrieves a JSON response and parses it into the specified type.
     *
     * @return The parsed JSON object of type T.
     */
    suspend inline fun <reified T> HttpResponse.getJson(): T =
        gson.fromJson(bodyAsText(), T::class.java)
}