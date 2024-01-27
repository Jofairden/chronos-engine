package chronos.engine.implementation.api

import chronos.engine.core.dsl.internalServerError
import chronos.engine.core.dsl.notFound
import chronos.engine.core.dsl.respondBody
import chronos.engine.core.interfaces.apis.IExternalApi
import chronos.engine.core.interfaces.apis.IExternalApiRequest
import chronos.engine.implementation.core.RoutesImpl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext
import io.ktor.utils.io.errors.IOException
import org.openapitools.client.infrastructure.ApiClient
import java.lang.reflect.Type
import kotlin.coroutines.cancellation.CancellationException
import io.ktor.client.statement.HttpResponse as KtorHttpResponse
import org.openapitools.client.infrastructure.HttpResponse as OpenApiHttpResponse

/**
 * Represents an abstract class for an external API.
 */
abstract class ExternalApi : IExternalApi, RoutesImpl() {
  abstract override val name: String
  abstract override val gson: Gson
  abstract override val client: HttpClient
  protected abstract val coreApi: ApiClient

  /**
   * Retrieves a JSON response and parses it to the specified type using a TypeToken.
   *
   * @param T The type of object to parse the JSON response into.
   * @param block A lambda function that returns the TypeToken representing the type to parse.
   * @return The parsed JSON object of type T.
   */
  suspend inline fun <reified T> KtorHttpResponse.getJsonWithTypeToken(block: () -> Type): T {
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
  suspend inline fun <reified T> KtorHttpResponse.getJsonCollection() = getJsonWithTypeToken<Collection<T>> {
    object : TypeToken<Collection<T>>() {}.type
  }

  /**
   * Retrieves a JSON response and parses it into the specified type.
   *
   * @return The parsed JSON object of type T.
   */
  suspend inline fun <reified T : Any> KtorHttpResponse.getJson(): T = gson.fromJson(bodyAsText(), T::class.java)

  /**
   * Executes an HTTP request inside the external API.
   *
   * @param block A lambda function that defines the behavior to be performed on the HTTP response.
   *              The lambda function is invoked with the `HttpResponse` object as the receiver,
   *              allowing direct access to the response properties and methods.
   *
   * @return The HTTP response object representing the response from the API request.
   */
  override suspend fun IExternalApiRequest.execute(block: suspend KtorHttpResponse.() -> Unit): KtorHttpResponse {
    return client.get(endpoint).let {
      it.block()
      it
    }
  }

  /**
   * Handles the response of an OpenApiHttpResponse by sending appropriate responses to the ApplicationCall.
   * This method is a suspend function and should be called within a coroutine.
   *
   * @param pipeline The PipelineContext of type Unit and ApplicationCall.
   * @param T The type of the OpenApiHttpResponse body.
   * @throws IOException if there is an error in reading the response body.
   * @throws CancellationException if the coroutine is cancelled.
   */ // TODO implement more status codes
  suspend inline fun <reified T : Any> OpenApiHttpResponse<T>.handle(pipeline: PipelineContext<Unit, ApplicationCall>) {
    with(pipeline) {
      try {
        when (response.status) {
          HttpStatusCode.OK -> respondBody<T>(response.body())
          HttpStatusCode.NotFound -> notFound("400")
          else -> internalServerError("500")
        }
      } catch (ex: IOException) {
        internalServerError("500")
      } catch (ex: CancellationException) {
        internalServerError("500")
      }
    }
  }
}
