package chronos.engine.configuration

import chronos.engine.core.dsl.asLoggable
import chronos.engine.core.gson.adapters.LocalDateTimeTypeAdapter
import chronos.engine.core.gson.adapters.LocalDateTypeAdapter
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cache.storage.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Configuration class for HttpClient.
 * This class configures the HttpClient with various components and settings such as gson and logging
 * @TODO In the future this will be able to deploy separate HttpClients for each API
 */
@Configuration
class HttpClientConfiguration {

    @Bean
    fun gson(): Gson = Gson().newBuilder().buildGson().create()

    fun GsonBuilder.buildGson(): GsonBuilder = with(this) {
        serializeNulls()
        setLenient()
        setDateFormat("yyyy-MM-dd HH:mm:ss")
        setPrettyPrinting()
        registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
        registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
        setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    }

    @Bean
    fun httpClient() = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
            }
        }
        with(this@HttpClient) {
            install(Logging) {
                logger = object : Logger {
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