package chronos.engine.core.dsl

import chronos.engine.implementation.logging.LogLevel
import chronos.engine.implementation.logging.Loggable
import chronos.engine.implementation.logging.LoggableTests
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
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
        val loggable =
            spyk(
                asLoggable("Test") {},
            )
        every { loggable.logger } returns logger
        every { loggable.info() } answers { callOriginal() }
        loggable.info()
        verify(exactly = 1) { logger.info("Test") }
    }

    @Test
    fun `test loggable log function for INFO level`() {
        mock.log(LogLevel.INFO)
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
        mock.log(LogLevel.ERROR)
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
        mock.log(LogLevel.TRACE)
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
        mock.log(LogLevel.DEBUG)
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
        mock.log(LogLevel.WARN)
        verify(exactly = 1) { logger.warn(any()) }
        verify(exactly = 0) {
            logger.error(any<String>())
            logger.trace(any<String>())
            logger.debug(any<String>())
            logger.info(any<String>())
        }
    }
}
