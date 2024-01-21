package chronos.engine.core.interfaces.apis

import io.ktor.client.request.HttpRequestBuilder

interface IExternalApiAuthenticator {
    fun HttpRequestBuilder.authenticate()
}
