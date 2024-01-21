package chronos.engine.implementation.logging

import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ExtendWith(MockKExtension::class)
class LoggingCtxTests {

    lateinit var logger: Logger

    lateinit var mock: LoggingContext<Logger, Loggable<Any>>

    lateinit var loggable: Loggable<Any>

    @BeforeEach
    fun setUp() {
        logger = spyk(LoggerFactory.getLogger(LoggingCtxTests::class.java))
        loggable = mockk(relaxed = true)
        mock = spyk(LoggingContext(logger, loggable))
        MockKAnnotations.init(this)

        every { loggable.loggable } returns "Mock Value"
        every { loggable.logger } returns logger
        every { loggable.getMessage() } returns "Mock Test"
        every { loggable.info() } answers { callOriginal() }
        every { loggable.trace() } answers { callOriginal() }
        every { loggable.warn() } answers { callOriginal() }
        every { loggable.debug() } answers { callOriginal() }
        every { loggable.error() } answers { callOriginal() }
        every { loggable.log(any()) } answers { callOriginal() }

        every { mock.info() } answers { callOriginal() }
        every { mock.trace() } answers { callOriginal() }
        every { mock.warn() } answers { callOriginal() }
        every { mock.debug() } answers { callOriginal() }
        every { mock.error() } answers { callOriginal() }
        every { mock.log(any()) } answers { callOriginal() }
    }

    @Test
    fun `loggable waarde is correct`() {
        assertEquals("Mock Test", mock.loggable.getMessage())
        assertEquals("Mock Value", mock.loggable.loggable)
    }

    @Test
    fun `info roept info log aan`() {
        mock.info()
        verify(exactly = 1) {
            mock.info()
            logger.info(any())
        }
        confirmVerified(mock)
    }

    @Test
    fun `warn roept warn log aan`() {
        mock.warn()
        verify(exactly = 1) {
            mock.warn()
            logger.warn(any())
        }
        confirmVerified(mock)
    }

    @Test
    fun `error roept error log aan`() {
        mock.error()
        verify(exactly = 1) {
            mock.error()
            logger.error(any())
        }
        confirmVerified(mock)
    }

    @Test
    fun `trace roept trace log aan`() {
        mock.trace()
        verify(exactly = 1) {
            mock.trace()
            logger.trace(any())
        }
        confirmVerified(mock)
    }

    @Test
    fun `debug roept debug log aan`() {
        mock.debug()
        verify(exactly = 1) {
            mock.debug()
            logger.debug(any())
        }
        confirmVerified(mock)
    }

    @Test
    fun `info roept geen andere levels aan`() {
        mock.info()
        verify(exactly = 0) {
            logger.debug(any())
            logger.error(any())
            logger.warn(any())
            logger.trace(any())
        }
    }

    @Test
    fun `debug roept geen andere levels aan`() {
        mock.debug()
        verify(exactly = 0) {
            logger.info(any())
            logger.error(any())
            logger.warn(any())
            logger.trace(any())
        }
    }

    @Test
    fun `error roept geen andere levels aan`() {
        mock.error()
        verify(exactly = 0) {
            logger.info(any())
            logger.debug(any())
            logger.warn(any())
            logger.trace(any())
        }
    }

    @Test
    fun `warn roept geen andere levels aan`() {
        mock.warn()
        verify(exactly = 0) {
            logger.info(any())
            logger.debug(any())
            logger.error(any())
            logger.trace(any())
        }
    }

    @Test
    fun `trace roept geen andere levels aan`() {
        mock.trace()
        verify(exactly = 0) {
            logger.info(any())
            logger.debug(any())
            logger.error(any())
            logger.warn(any())
        }
    }

    @Test
    fun `log roept juiste niveau aan (info)`() {
        mock.log(LogLevel.INFO)
        verify(exactly = 1) {
            logger.info(any())
        }
    }

    @Test
    fun `log roept juiste niveau aan (debug)`() {
        mock.log(LogLevel.DEBUG)
        verify(exactly = 1) {
            logger.debug(any())
        }
    }

    @Test
    fun `log roept juiste niveau aan (warn)`() {
        mock.log(LogLevel.WARN)
        verify(exactly = 1) {
            logger.warn(any())
        }
    }

    @Test
    fun `log roept juiste niveau aan (trace)`() {
        mock.log(LogLevel.TRACE)
        verify(exactly = 1) {
            logger.trace(any())
        }
    }

    @Test
    fun `log roept juiste niveau aan (error)`() {
        mock.log(LogLevel.ERROR)
        verify(exactly = 1) {
            logger.error(any())
        }
    }

    @Test
    fun `log geeft juiste gegevens door aan logger`() {
        every { loggable.getMessage() } returns "Test Waarde"
        mock.log(LogLevel.INFO)
        verify(exactly = 1) {
            logger.info("Test Waarde")
        }
    }
    @Test
    fun `log geeft juiste gegevens door aan logger bij onbekende waarde`() {
        every { loggable.getMessage() } returns "Test Waarde"
        mock.log(LogLevel("Bestaat Niet"))
        verify(exactly = 1) {
            logger.trace("Test Waarde")
        }
    }

    @Test
    fun `companion functie of maakt juiste object aan`() {
        mockkObject(LoggingContext.Companion) {
            every { LoggingContext.Companion.of(any()) } answers { callOriginal() }

            val waarde = LoggingContext.Companion.of(loggable)

            verify(exactly = 1) {
                LoggingContext.Companion.of(any())
            }
            assertTrue {
                waarde.logger !== logger
                waarde.loggable === loggable
            }
        }

    }


}