package chronos.engine.core.interfaces

import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

/**
 * IRoutes is an interface for defining routes in an application. It provides a single function to retrieve the value of a parameter from a call's parameters.
 */
interface IRoutes {
  fun PipelineContext<Unit, ApplicationCall>.param(name: String) : String?
  fun PipelineContext<Unit, ApplicationCall>.queryParam(name: String) : String?
}
