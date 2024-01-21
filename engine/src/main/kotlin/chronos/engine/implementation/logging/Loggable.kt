package chronos.engine.implementation.logging

import chronos.engine.core.interfaces.ILogLevel
import chronos.engine.core.interfaces.ILoggable
import chronos.engine.util.Reflection
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class Loggable<out T : Any>(
    override val loggable: T,
) : ILoggable<T> {
    val logger: Logger = LoggerFactory.getLogger(Reflection.getCallerClassName())

    override fun getMessage(): String = loggable.toString()

    override fun <A : ILogLevel> log(level: A): Unit =
        when (level) {
            LogLevel.INFO -> info()
            LogLevel.DEBUG -> debug()
            LogLevel.WARN -> warn()
            LogLevel.ERROR -> error()
            LogLevel.TRACE -> trace()
            else -> trace()
        }

    override fun trace() {
        logger.trace(getMessage())
    }

    override fun debug() {
        logger.debug(getMessage())
    }

    override fun info() {
        logger.info(getMessage())
    }

    override fun warn() {
        logger.warn(getMessage())
    }

    override fun error() {
        logger.error(getMessage())
    }
}
