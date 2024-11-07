package chronos.engine.infrastructure.modules

import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.requests.GatewayIntent
import org.koin.dsl.module

val chronosModule =
  module(createdAtStart = true) {
    // Initialiseer de JDA client builder
    single<JDA> {
      light(getProperty("discord.token"), enableCoroutines = true) {
        intents += listOf(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
      }
    }
  }

// Initialiseer de main module
val engineModule =
  module(createdAtStart = true) {
    includes(
      httpClientModule,
      gsonModule,
      apiModules,
      chronosModule,
      botModules
    )
  }
