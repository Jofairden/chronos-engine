package chronos.engine

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
@ImportAutoConfiguration(ExposedAutoConfiguration::class)
class EngineApplication {
    companion object {
        lateinit var ctx: ConfigurableApplicationContext

        @JvmStatic
        fun main(args: Array<String>): Unit {
            ctx = runApplication<EngineApplication>(*args) {
                setBannerMode(Banner.Mode.OFF)
            }
        }
    }
}

