package chronos.engine.core.logging

import arrow.optics.optics

/**
 * Definieert mogelijke log niveaus die bestaan in de applicatie
 */
@optics
enum class LoggableLevel {
  DEBUG,
  INFO,
  WARN,
  ERROR,
  TRACE
}
