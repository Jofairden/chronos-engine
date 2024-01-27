package chronos.engine.core.dsl

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext

public suspend inline fun <reified T: Any> PipelineContext<Unit, ApplicationCall>.internalServerError(message: T) {
	call.respond(HttpStatusCode.InternalServerError, message)
}
public suspend inline fun <reified T: Any> PipelineContext<Unit, ApplicationCall>.notFound(message: T) {
	call.respond(HttpStatusCode.NotFound, message)
}
public suspend inline fun <reified T: Any> PipelineContext<Unit, ApplicationCall>.respondBody(body: T) {
	call.respond(body)
}

