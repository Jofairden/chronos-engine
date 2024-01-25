package chronos.engine.plugins

import io.ktor.server.application.Application

fun Application.main() {
    configureKoin()

    configureResources()
    configureRouting()
    configureSerialization()

    installCors()
}
