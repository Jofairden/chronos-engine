package chronos.engine

import chronos.engine.modules.coincodexHttpModule
import chronos.engine.modules.coincodexModule
import chronos.engine.modules.gsonModule
import chronos.engine.modules.httpClientModule
import chronos.engine.modules.swapzoneHttpModule
import chronos.engine.modules.swapzoneModule
import chronos.engine.plugins.main
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.environmentProperties
import org.koin.fileProperties
import org.koin.logger.SLF4JLogger

@Suppress(
    "UtilityClassWithPublicConstructor",
    "SpreadOperator",
)
class EngineApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            startKoin {
                logger(SLF4JLogger())
                fileProperties()
                modules(engineModule)
                environmentProperties()
                createEagerInstances()
            }

            embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
                main()
            }.start(wait = true)
        }
    }
}

val engineModule =
    module(createdAtStart = true) {
        includes(
            httpClientModule,
            gsonModule,
            coincodexModule,
            coincodexHttpModule,
            swapzoneModule,
            swapzoneHttpModule,
        )
    }
