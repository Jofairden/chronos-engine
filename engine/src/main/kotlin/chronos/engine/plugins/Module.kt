package chronos.engine.plugins

import io.ktor.server.application.Application

fun Application.main() {
  configureKoin()

  installResources()
  installRouting()
  installSerialization()
  installCors()
  installDataConversion()
}
