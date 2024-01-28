package chronos.engine.modules

import chronos.engine.core.dsl.asLoggable
import chronos.engine.core.dsl.isInternalServerError
import chronos.engine.core.dsl.isRequestTimeout
import chronos.engine.core.gson.adapters.LocalDateTimeTypeAdapter
import chronos.engine.core.gson.adapters.LocalDateTypeAdapter
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.logging.Logger
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson
import org.koin.dsl.module
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.reflect.typeOf

/**
 * Builds a Gson instance with specific configurations.
 *
 * @return the modified GsonBuilder object
 */
fun GsonBuilder.buildGson(): GsonBuilder =
  with(this) {
    serializeNulls()
    setLenient()
    setDateFormat("yyyy-MM-dd HH:mm:ss")
    setPrettyPrinting()
    registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
    registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
    setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
  }

inline fun <reified T : HttpClientEngineConfig> HttpClientConfig<T>.buildDefaultHttpClient() = apply {
  if (typeOf<T>() == OkHttpConfig::class.java) {
    with(this as HttpClientConfig<OkHttpConfig>) {
      engine {
        config {
          followRedirects(true)
        }
      }
    }
  }

  with(this) {
    install(io.ktor.client.plugins.logging.Logging) {
      logger = object : Logger {
        override fun log(message: String) {
          asLoggable(message) { info() }
        }
      }
      level = io.ktor.client.plugins.logging.LogLevel.ALL
      sanitizeHeader { header ->
        header == io.ktor.http.HttpHeaders.Authorization
      }
    }
    install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
      gson {
        buildGson()
      }
    }
    install(io.ktor.client.plugins.compression.ContentEncoding) {
      deflate(1.0F)
      gzip(0.9F)
    }
    install(io.ktor.client.plugins.UserAgent) {
      agent = "Chronos Engine"
    }
    install(io.ktor.client.plugins.HttpRequestRetry) {
      maxRetries = 3
      retryIf { request, response ->
        !response.status.isSuccess()
          && (response.status.isInternalServerError()
          || response.status.isRequestTimeout())
      }
      retryOnExceptionIf { request, cause ->
        cause is RedirectResponseException
      }
      delayMillis { retry ->
        retry * 3000L
      } // retries in 3, 6, 9, etc. seconds
    }
    install(io.ktor.client.plugins.cache.HttpCache) {
      val cacheFile = java.nio.file.Files.createDirectories(java.nio.file.Paths.get("build/cache")).toFile()
      publicStorage(FileStorage(cacheFile))
    }
  }
}

/**
 * Generates an instance of HttpClient with specific configurations.
 * @return the generated HttpClient instance
 */
fun generateHttpClient(block: HttpClientConfig<*>.() -> Unit = {}) = HttpClient(OkHttp) {
  buildDefaultHttpClient()
  block()
}


val gsonModule =
  module {
    single<Gson> {
      Gson().newBuilder().buildGson().create()
    }
  }

val httpClientModule =
  module {
    single<HttpClient> {
      generateHttpClient()
    }
  }
