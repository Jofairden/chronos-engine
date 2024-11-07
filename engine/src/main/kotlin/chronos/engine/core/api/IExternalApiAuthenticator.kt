package chronos.engine.core.api

import io.ktor.client.request.HttpRequestBuilder

interface IExternalApiAuthenticator {
  fun HttpRequestBuilder.authenticate()
}
