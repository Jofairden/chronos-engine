package chronos.engine.configuration

import chronos.engine.core.dsl.asLoggable
import chronos.engine.core.gson.adapters.LocalDateTimeTypeAdapter
import chronos.engine.core.gson.adapters.LocalDateTypeAdapter
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("MagicNumber")
/**
 * Configuration class for HttpClient.
 * This class configures the HttpClient with various components and settings such as gson and logging
 * @TODO In the future this will be able to deploy separate HttpClients for each API
 */
@Configuration
class HttpClientConfiguration {
    @Bean
    fun gson(): Gson = Gson().newBuilder().buildGson().create()

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

    @Bean
    fun httpClient() =
        HttpClient(OkHttp) {
            engine {
                config {
                    followRedirects(true)
                }
            }
            with(this@HttpClient) {
                install(Logging) {
                    logger =
                        object : Logger {
                            override fun log(message: String) {
                                asLoggable(message) { info() }
                            }
                        }
                    level = LogLevel.NONE
                    sanitizeHeader { header ->
                        header == HttpHeaders.Authorization
                    }
                }
                install(ContentNegotiation) {
                    gson {
                        buildGson()
                    }
                }
                install(ContentEncoding) {
                    deflate(1.0F)
                    gzip(0.9F)
                }
                install(UserAgent) {
                    agent = "Chronos Engine"
                }
                install(HttpRequestRetry) {
                    maxRetries = 3
                    retryIf { request, response ->
                        !response.status.isSuccess()
                    }
                    retryOnExceptionIf { request, cause ->
                        cause is RedirectResponseException
                    }
                    delayMillis { retry ->
                        retry * 3000L
                    } // retries in 3, 6, 9, etc. seconds
                }
                install(HttpCache) {
                    val cacheFile = Files.createDirectories(Paths.get("build/cache")).toFile()
                    publicStorage(FileStorage(cacheFile))
                }
            }
        }
}
