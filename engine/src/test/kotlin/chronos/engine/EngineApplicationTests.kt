package chronos.engine

import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules
import org.koin.test.junit5.AutoCloseKoinTest
import org.koin.test.mock.MockProvider

class EngineApplicationTests : AutoCloseKoinTest() {
  val testModule =
    module {
      includes(
        engineModule,
      )
      single<String>(named("coincodex.api.baseurl"), createdAtStart = true) {
        "coincodex"
      }
      single<String>(named("swapzone.api.baseurl"), createdAtStart = true) {
        "swapzone"
      }
      single<String>(named("swapzone.api.key"), createdAtStart = true) {
        "swapzone.key"
      }
    }

  @Test
  fun checkKoinModule() {
    if (GlobalContext.getOrNull() == null) {
      koinApplication {
//                fileProperties("test.properties")
        modules(testModule)
        MockProvider.register { clazz -> mockkClass(clazz) }
        checkModules()
      }
    }
  }
}
