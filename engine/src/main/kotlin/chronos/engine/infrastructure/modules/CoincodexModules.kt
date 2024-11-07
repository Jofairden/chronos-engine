package chronos.engine.infrastructure.modules

import chronos.engine.domain.api.coincodex.CoincodexScheduler
import chronos.engine.domain.api.coincodex.CoincodexService
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
import org.openapi.Coincodex.api.CoincodexApi

private val coincodexHttpModule =
  module {
    scope<CoincodexService> {
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

val coincodexModule =
  module {
    single<String>(named("coincodex.api.baseurl"), createdAtStart = true) {
      getProperty("coincodex.api.baseurl")
    }
    singleOf(::CoincodexService) { createdAtStart() }
    singleOf(::CoincodexScheduler) { createdAtStart() }
    single {
      CoincodexApi(
        httpClientConfig = {
          with(it as HttpClientConfig<OkHttpConfig>) { buildDefaultHttpClient() }
        },
        jsonBlock = {
          buildGson()
        },
      )
    }
    includes(coincodexHttpModule)
  }
