package chronos.engine.implementation.apis.coincodex

import chronos.engine.implementation.services.HTTPClientService
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ExtendWith(MockKExtension::class)
@SpringBootTest
internal class CoincodexSchedulerTest {

    @MockBean
    private lateinit var httpClientService: HTTPClientService

    @MockBean
    private lateinit var api: CoincodexApi

    private lateinit var underTest: CoincodexScheduler

}