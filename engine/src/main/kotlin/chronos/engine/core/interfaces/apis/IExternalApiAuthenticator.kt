package chronos.engine.core.interfaces.apis

import io.ktor.client.request.*

interface IExternalApiAuthenticator {
    fun HttpRequestBuilder.authenticate() : Unit
}