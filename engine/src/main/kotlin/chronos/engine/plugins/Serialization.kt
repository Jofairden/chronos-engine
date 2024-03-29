package chronos.engine.plugins

import chronos.engine.modules.buildGson
import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun Application.installSerialization() {
  install(ContentNegotiation) {
    gson {
      buildGson()
    }
  }
}
