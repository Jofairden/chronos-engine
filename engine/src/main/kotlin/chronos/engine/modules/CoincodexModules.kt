package chronos.engine.modules

import chronos.engine.implementation.api.coincodex.CoincodexApi
import chronos.engine.implementation.api.coincodex.CoincodexScheduler
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.openapi.coincodex.api.DefaultApi

val coincodexModule =
    module {
        single<String>(named("coincodex.api.baseurl"), createdAtStart = true) {
            getProperty("coincodex.api.baseurl")
        }
        singleOf(::CoincodexApi) { createdAtStart() }
        singleOf(::CoincodexScheduler) { createdAtStart() }
        single { DefaultApi(
            httpClientConfig =  {
                with(it as HttpClientConfig<OkHttpConfig>) { buildDefaultHttpClient() }
            },
            jsonBlock = {
                buildGson()
            }
        ) }
    }

val coincodexHttpModule =
    module {
        scope<CoincodexApi> {
            scoped {
                generateHttpClient {
                    defaultRequest {
                        val baseUrl by inject<String>(named("coincodex.api.baseurl"))
                        url {
                            host = baseUrl
                            protocol = URLProtocol.HTTPS
                        }
                        headers {
                            append(HttpHeaders.Accept, "application/json")
                            append(HttpHeaders.UserAgent, "Chronos Engine Coincodex API")
                        }
                    }
                }
            }
        }
    }
