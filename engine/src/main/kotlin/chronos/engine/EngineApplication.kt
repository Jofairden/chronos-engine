package chronos.engine

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext

@Suppress(
    "detekt.style.UtilityClassWithPublicConstructor",
    "detekt.performance.SpreadOperator"
)
@SpringBootApplication
@ImportAutoConfiguration(ExposedAutoConfiguration::class)
class EngineApplication {
    companion object {
        lateinit var ctx: ConfigurableApplicationContext

        @JvmStatic
        fun main(args: Array<String>) {
            ctx =
                runApplication<EngineApplication>(*args) {
                    setBannerMode(Banner.Mode.OFF)
                }
        }
    }
}
