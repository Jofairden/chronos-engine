package chronos.engine.core.dsl

import chronos.engine.core.logging.Loggable
import chronos.engine.core.logging.LoggingContext
import org.slf4j.Logger

/**
 * Converts a value into a loggable object and applies a block of code to it.
 * Can be used to log any .toString()-able object, for example:
 * asLoggable("Hello world!") { info() } // Prints: Hello world! at INFO level
 * @param value the value to be converted into a loggable object
 * @param block the block of code to be applied to the loggable object
 * @return the loggable object after applying the block of code
 */
fun <T : Any> log(
  value: T,
  block: LoggingContext<Logger, Loggable<T>>.() -> Unit = {},
): Loggable<T> {
  val loggable = Loggable(value)
  val ctx = LoggingContext.of(loggable)
  return ctx.apply { this.block() }.let { loggable }
}
