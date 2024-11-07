package chronos.engine

import chronos.engine.infrastructure.modules.engineModule
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import org.koin.core.context.GlobalContext
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.environmentProperties
import org.koin.fileProperties
import org.koin.test.check.checkModules
import org.koin.test.junit5.AutoCloseKoinTest
import org.koin.test.mock.MockProvider

class ChronosApplicationTests : AutoCloseKoinTest() {
  val testModule =
    module {
      includes(
        engineModule,
      )
    }

  @Test
  fun checkKoinModule() {
    if (GlobalContext.getOrNull() == null) {
      koinApplication {
        fileProperties()
        modules(testModule)
        environmentProperties()
        createEagerInstances()
        MockProvider.register { clazz -> mockkClass(clazz) }
        checkModules()
      }
    }
  }
}
