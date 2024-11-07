package chronos.engine.core.logging

import arrow.optics.optics

// Definieert mogelijke log niveaus die bestaan in de applicatie
@optics
data class LogLevel(
  override val name: String,
) : ILogLevel {
  companion object {
    val DEBUG = LogLevel("DEBUG")
    val INFO = LogLevel("INFO")
    val WARN = LogLevel("WARN")
    val ERROR = LogLevel("ERROR")
    val TRACE = LogLevel("TRACE")
  }
}
