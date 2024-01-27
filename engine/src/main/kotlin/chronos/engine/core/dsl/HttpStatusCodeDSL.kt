package chronos.engine.core.dsl

import io.ktor.http.HttpStatusCode

fun HttpStatusCode.isNotFound() = this == HttpStatusCode.NotFound
fun HttpStatusCode.isBadRequest() = this == HttpStatusCode.BadRequest
fun HttpStatusCode.isRequestTimeout() = this == HttpStatusCode.RequestTimeout
fun HttpStatusCode.isInternalServerError() = this == HttpStatusCode.InternalServerError
