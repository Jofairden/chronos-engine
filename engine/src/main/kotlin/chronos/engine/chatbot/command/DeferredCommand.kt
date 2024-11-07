package chronos.engine.chatbot.command

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import kotlin.coroutines.CoroutineContext

abstract class DeferredCommand : BotCommand() {

  open fun coroutineContext(): CoroutineContext = Dispatchers.Default

  abstract suspend fun deferredInvoke(event: GenericCommandInteractionEvent, args: List<String>)

  override fun invoke(event: GenericCommandInteractionEvent, args: List<String>) {
    event.deferReply()

    val scope = CoroutineScope(CoroutineName("command-job-${event.guild?.id}-${event.user.id}-${name}"))
    scope.launch(coroutineContext()) {
      deferredInvoke(event, args)
    }
  }
}
