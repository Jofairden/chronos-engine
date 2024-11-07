package chronos.engine.core.logging

import arrow.optics.optics
import org.slf4j.Logger

@optics
data class LoggingContext<A : Logger, B : Loggable<*>>(
  val logger: A,
  val loggable: B,
) {
  companion object {
    fun <B : Loggable<*>> of(loggable: B): LoggingContext<Logger, B> = LoggingContext(loggable.logger, loggable)
  }

  fun <A : LoggableLevel> log(level: A): Unit = loggable.log(level)

  fun info() = loggable.info()

  fun trace() = loggable.trace()

  fun debug() = loggable.debug()

  fun warn() = loggable.warn()

  fun error() = loggable.error()
}
