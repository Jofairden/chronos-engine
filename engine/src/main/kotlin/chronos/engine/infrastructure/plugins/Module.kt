package chronos.engine.infrastructure.plugins

import io.ktor.server.application.Application

fun Application.main() {
  configureKoin()
  configureAutoHeadResponse()

  installResources()
  installRouting()
  installSerialization()
  installCors()
  installDataConversion()
  installRateLimits()
}
