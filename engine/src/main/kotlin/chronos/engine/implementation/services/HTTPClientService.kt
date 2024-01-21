package chronos.engine.implementation.services

import chronos.engine.core.dsl.asLoggable
import chronos.engine.implementation.logging.LogLevel
import io.ktor.client.HttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Service

@Service
class HTTPClientService @Autowired constructor(
    val client: HttpClient
) : ApplicationListener<ContextRefreshedEvent> {

    @Override
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        asLoggable("[onApplicationEvent] HTTP Client Created") {
            log(LogLevel.INFO)
        }
    }
}