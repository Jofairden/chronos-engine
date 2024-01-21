package chronos.engine.implementation.logging

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@ExtendWith(MockKExtension::class)
class LoggableTests {
    @SpyK
    var logger: Logger = LoggerFactory.getLogger(LoggableTests::class.java)

    @MockK(relaxed = true)
    lateinit var mock: Loggable<Any>

    @BeforeEach
    fun setUp() {
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
    fun `loggable value is correct`() {
        assertEquals("Mock Test", mock.loggable)
        verify { mock.loggable }
        confirmVerified(mock)
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
    fun `loggable calls error`() {
        mock.error()
        verify(exactly = 1) { logger.error(any()) }
    }

    @Test
    fun `loggable calls trace`() {
        mock.trace()
        verify(exactly = 1) { logger.trace(any()) }
    }

    @Test
    fun `loggable calls debug`() {
        mock.debug()
        verify(exactly = 1) { logger.debug(any()) }
    }

    @Test
    fun `info does not call other levels`() {
        mock.info()
        verify(exactly = 0) {
            logger.debug(any())
            logger.error(any())
            logger.warn(any())
            logger.trace(any())
        }
    }

    @Test
    fun `debug does not call other levels`() {
        mock.debug()
        verify(exactly = 0) {
            logger.info(any())
            logger.error(any())
            logger.warn(any())
            logger.trace(any())
        }
    }

    @Test
    fun `error does not call other levels`() {
        mock.error()
        verify(exactly = 0) {
            logger.info(any())
            logger.debug(any())
            logger.warn(any())
            logger.trace(any())
        }
    }

    @Test
    fun `warn does not call other levels`() {
        mock.warn()
        verify(exactly = 0) {
            logger.info(any())
            logger.debug(any())
            logger.error(any())
            logger.trace(any())
        }
    }

    @Test
    fun `trace does not call other levels`() {
        mock.trace()
        verify(exactly = 0) {
            logger.info(any())
            logger.debug(any())
            logger.error(any())
            logger.warn(any())
        }
    }

    @Test
    fun `log calls correct level (info)`() {
        mock.log(LogLevel.INFO)
        verify(exactly = 1) {
            logger.info(any())
        }
    }

    @Test
    fun `log calls correct level (debug)`() {
        mock.log(LogLevel.DEBUG)
        verify(exactly = 1) {
            logger.debug(any())
        }
    }

    @Test
    fun `log calls correct level (warn)`() {
        mock.log(LogLevel.WARN)
        verify(exactly = 1) {
            logger.warn(any())
        }
    }

    @Test
    fun `log calls correct level (trace)`() {
        mock.log(LogLevel.TRACE)
        verify(exactly = 1) {
            logger.trace(any())
        }
    }

    @Test
    fun `log calls correct level (error)`() {
        mock.log(LogLevel.ERROR)
        verify(exactly = 1) {
            logger.error(any())
        }
    }

    @Test
    fun `log passes correct data to logger`() {
        every { mock.getMessage() } returns "Test Value"
        mock.log(LogLevel.INFO)
        verify(exactly = 1) {
            logger.info(any())
        }
    }

    @Test
    fun `log passes correct data to logger for unknown value`() {
        every { mock.getMessage() } returns "Test Value"
        mock.log(LogLevel("Does Not Exist"))
        verify(exactly = 1) {
            logger.trace("Test Value")
        }
    }
}