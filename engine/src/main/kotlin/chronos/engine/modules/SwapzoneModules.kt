package chronos.engine.modules

import chronos.engine.implementation.api.swapzone.SwapzoneApi
import chronos.engine.implementation.api.swapzone.SwapzoneScheduler
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val swapzoneModule =
    module {
        single<String>(named("swapzone.api.baseurl"), createdAtStart = true) {
            getProperty("swapzone.api.baseurl")
        }
        single<String>(named("swapzone.api.key"), createdAtStart = true) {
            getProperty("swapzone.api.key")
        }
        singleOf(::SwapzoneApi) { createdAtStart() }
        singleOf(::SwapzoneScheduler) { createdAtStart() }
    }

val swapzoneHttpModule =
    module {
        scope<SwapzoneApi> {
            scoped {
                generateHttpClient {
                    defaultRequest {
                        val baseUrl by inject<String>(named("swapzone.api.baseurl"))
                        val key by inject<String>(named("swapzone.api.key"))
                        url {
                            host = baseUrl
                            protocol = URLProtocol.HTTPS
                        }
                        headers {
                            append("x-api-key", key)
                            append(HttpHeaders.Accept, "application/json")
                            append(HttpHeaders.UserAgent, "Chronos Engine Swapzone API")
                        }
                    }
                }
            }
        }
    }
