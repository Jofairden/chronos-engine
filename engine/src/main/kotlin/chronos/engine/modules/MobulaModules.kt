package chronos.engine.modules

import chronos.engine.implementation.api.mobula.MobulaApi
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
import org.openapi.mobula.api.MobulaMarketApi

val mobulaModule =
  module {
    single<String>(named("mobula.api.baseurl"), createdAtStart = true) {
      getProperty("mobula.api.baseurl")
    }
    single<String>(named("mobula.api.key"), createdAtStart = true) {
      getProperty("mobula.api.key")
    }
    singleOf(::MobulaApi) { createdAtStart() }
    single<MobulaMarketApi>(createdAtStart = true) {
      val key by inject<String>(named("mobula.api.key"))
      MobulaMarketApi(
        httpClientConfig = {
          with(it as HttpClientConfig<OkHttpConfig>) {
            buildDefaultHttpClient()
          }
        },
        jsonBlock = { buildGson() }
      ).apply { setApiKey(key) }
    }
  }

val mobulaHttpModule =
  module {
    scope<MobulaApi> {
      scoped {
        generateHttpClient {
          defaultRequest {
            val baseUrl by inject<String>(named("mobula.api.baseurl"))
            val key by inject<String>(named("mobula.api.key"))
            url {
              host = baseUrl
              protocol = URLProtocol.HTTPS
            }
            headers {
              append(HttpHeaders.Accept, "application/json")
              append(HttpHeaders.UserAgent, "Chronos Engine Mobula API")
              append(HttpHeaders.Authorization, key)
            }
          }
        }
      }
    }
  }
