package chronos.engine.core.dsl

import chronos.engine.core.logging.LoggableLevel
import chronos.engine.core.logging.Loggable
import chronos.engine.core.logging.LoggableTests
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ExtendWith(MockKExtension::class)
class LoggingDslTests {
  @SpyK
  var logger: Logger = LoggerFactory.getLogger(LoggableTests::class.java)

  @MockK(relaxed = true)
  lateinit var mock: Loggable<Any>

  @BeforeEach
  fun setup() {
    MockKAnnotations.init(this)
    every { mock.loggable } returns "Mock Test"
    every { mock.logger } returns logger
    every { mock.getMessage() } answers { callOriginal() }
    every { mock.info() } answers { callOriginal() }
    every { mock.trace() } answers { callOriginal() }
    every { mock.warn() } answers { callOriginal() }
    every { mock.debug() } answers { callOriginal() }
    every { mock.error() } answers { callOriginal() }
    every { mock.log(any()) } answers { callOriginal() }
  }

  @Test
  fun `loggable calls info`() {
    mock.info()
    verify(exactly = 1) { logger.info(any()) }
  }

  @Test
  fun `loggable calls warn`() {
    mock.warn()
    verify(exactly = 1) { logger.warn(any()) }
  }

  @Test
  fun `loggable calls debug`() {
    mock.debug()
    verify(exactly = 1) { logger.debug(any()) }
  }

  @Test
  fun `loggable calls trace`() {
    mock.trace()
    verify(exactly = 1) { logger.trace(any()) }
  }

  @Test
  fun `test loggable log function for INFO level`() {
    mock.log(LoggableLevel.INFO)
    verify(exactly = 1) { logger.info(any()) }
    verify(exactly = 0) {
      logger.error(any<String>())
      logger.trace(any<String>())
      logger.debug(any<String>())
      logger.warn(any<String>())
    }
  }

  @Test
  fun `test loggable log function for ERROR level`() {
    mock.log(LoggableLevel.ERROR)
    verify(exactly = 1) { logger.error(any()) }
    verify(exactly = 0) {
      logger.info(any<String>())
      logger.trace(any<String>())
      logger.debug(any<String>())
      logger.warn(any<String>())
    }
  }

  @Test
  fun `test loggable log function for TRACE level`() {
    mock.log(LoggableLevel.TRACE)
    verify(exactly = 1) { logger.trace(any()) }
    verify(exactly = 0) {
      logger.error(any<String>())
      logger.info(any<String>())
      logger.debug(any<String>())
      logger.warn(any<String>())
    }
  }

  @Test
  fun `test loggable log function for DEBUG level`() {
    mock.log(LoggableLevel.DEBUG)
    verify(exactly = 1) { logger.debug(any()) }
    verify(exactly = 0) {
      logger.error(any<String>())
      logger.trace(any<String>())
      logger.info(any<String>())
      logger.warn(any<String>())
    }
  }

  @Test
  fun `test loggable log function for WARN level`() {
    mock.log(LoggableLevel.WARN)
    verify(exactly = 1) { logger.warn(any()) }
    verify(exactly = 0) {
      logger.error(any<String>())
      logger.trace(any<String>())
      logger.debug(any<String>())
      logger.info(any<String>())
    }
  }
}
