package chronos.engine.core.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A loggable object with type T
 * @param T Any type
 */
open class Loggable<out T : Any>(
  override val loggable: T,
) : ILoggable<T> {
  /**
   * The logger that belongs to the caller of this loggable instance
   */
  val logger: Logger = LoggerFactory.getLogger(getCallerClassName())

  /**
   * Returns the toString() value of the loggable value
   */
  override fun getMessage(): String = loggable.toString()

  /**
   * Logs the message at the specified level
   * @param level The level to log as (default=trace)
   */
  override fun <A : LoggableLevel> log(level: A): Unit =
    when (level) {
      LoggableLevel.INFO -> info()
      LoggableLevel.DEBUG -> debug()
      LoggableLevel.WARN -> warn()
      LoggableLevel.ERROR -> error()
      LoggableLevel.TRACE -> trace()
      else -> trace()
    }

  /**
   * Prints the loggable as trace
   */
  override fun trace() {
    logger.trace(getMessage())
  }

  /**
   * Prints the loggable as debug
   */
  override fun debug() {
    logger.debug(getMessage())
  }

  /**
   * Prints the loggable as info
   */
  override fun info() {
    logger.info(getMessage())
  }

  /**
   * Prints the loggable as a warning
   */
  override fun warn() {
    logger.warn(getMessage())
  }

  /**
   * Prints the loggable as an error
   */
  override fun error() {
    logger.error(getMessage())
  }

  /**
   * Gets the fully qualified class name of the caller.
   *
   * @return the fully qualified class name of the caller, or null if the class name cannot be determined.
   */
  private fun getCallerClassName(): String? {
    val stElements = Thread.currentThread().stackTrace
    var callerClassName: String? = null
    for (i in 1 until stElements.size) {
      val ste = stElements[i]
      if (ste.className.indexOf("java.lang.Thread") != 0) {
        if (callerClassName == null) {
          callerClassName = ste.className
        } else if (callerClassName != ste.className &&
          ste.className != "chronos.engine.implementation.logging.Loggable" &&
          !ste.className.startsWith("chronos.engine.core.dsl.LoggingDSLKt")
        ) {
          callerClassName = ste.className
          break
        }
      }
    }
    return callerClassName
  }
}
