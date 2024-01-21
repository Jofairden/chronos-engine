package chronos.engine.implementation.logging

import arrow.optics.optics
import chronos.engine.core.interfaces.ILogLevel
import org.slf4j.Logger

@optics
data class LogCtx<A : Logger, B : Loggable<*>>(
    val logger: A,
    val loggable: B
) {
    companion object {
        fun <B : Loggable<*>> of(loggable: B): LogCtx<Logger, B>
                = LogCtx(loggable.logger, loggable)
    }

    fun <A : ILogLevel> log(level: A): Unit = loggable.log(level)
    fun info() = loggable.info()
    fun trace() = loggable.trace()
    fun debug() = loggable.debug()
    fun warn() = loggable.warn()
    fun error() = loggable.error()
}
