package chronos.engine.domain.core

import chronos.engine.core.routing.IRoutes
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

/**
 * RoutesImpl is an implementation of the IRoutes interface. It provides the implementation for the param() function, which
 * retrieves the value of a parameter from the call's parameters.
 */
open class RoutesImpl : IRoutes {
  override fun PipelineContext<Unit, ApplicationCall>.param(name: String): String? = call.parameters[name]

  override fun PipelineContext<Unit, ApplicationCall>.queryParam(name: String): String? = call.request.queryParameters[name]
}
