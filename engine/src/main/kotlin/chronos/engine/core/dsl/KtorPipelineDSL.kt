package chronos.engine.core.dsl

import chronos.engine.domain.api.ExternalApi
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext
import org.openapitools.client.infrastructure.HttpResponse
import java.io.Serializable

/**
 * Responds with an internal server error (HTTP status code 500) and a specified
 * error message. This method is a suspend function and should be called within a coroutine.
 *
 * @param T The type of the error message.
 * @param message The error message to be sent in the response body.
 */
suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.internalServerError(message: T) {
  call.respond(HttpStatusCode.InternalServerError, message)
}

/**
 * Responds to the client with a HTTP status code 404 (Not Found) and a given message.
 *
 * @param T The type of the message.
 * @param message The message to be sent as the response body.
 */
suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.notFound(message: T) {
  call.respond(HttpStatusCode.NotFound, message)
}

/**
 * Sends a response with the given body to the client. Returns status 200
 *
 * @param T The type of the body to be sent.
 * @param body The body to be sent as the response.
 */
suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.respondBody(body: T) {
  call.respond(body)
}

/**
 * Executes a route-specific API call and handles the response.
 *
 * @param T The type of the chronos.engine.domain.api.ExternalApi.
 * @param A The type of the HttpResponse.
 * @param api The instance of the chronos.engine.domain.api.ExternalApi to use for the API call.
 * @param block The lambda function that defines the route-specific API call.
 *
 * @throws IOException if there is an error in reading the response body.
 * @throws CancellationException if the coroutine is cancelled.
 */
suspend inline fun <reified T : ExternalApi, reified A : Serializable> PipelineContext<Unit, ApplicationCall>.process(
  api: T,
  block: T.() -> HttpResponse<A>,
) {
  with(api.block()) { // run route specific call and get response
    with(api) { // with api in context
      handle(this@process) // hanlde this process pipeline
    }
  }
}
