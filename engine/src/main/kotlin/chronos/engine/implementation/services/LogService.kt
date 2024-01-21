package chronos.engine.implementation.services

import chronos.engine.core.dsl.asLoggable
import chronos.engine.implementation.logging.LogLevel
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Service

@Service
class LogService : ApplicationListener<ContextRefreshedEvent> {
    @Override
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        asLoggable("[onApplicationEvent] Spring Context Initialized") {
            log(LogLevel.INFO)
        }
    }

    init {
        asLoggable("LogService initiated from init") {
            debug()
        }
    }
}