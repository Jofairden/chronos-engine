package chronos.engine.infrastructure.modules

import chronos.engine.chatbot.BotBuilder
import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import net.dv8tion.jda.api.requests.GatewayIntent
import org.koin.dsl.module

val chronosModule =
  module(createdAtStart = true) {
    // Init ChronosBuilder
    single {
      BotBuilder()
    }

    // Initialiseer de JDA client builder
    single {
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
    )
  }