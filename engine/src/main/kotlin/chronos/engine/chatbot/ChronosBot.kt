package chronos.engine.chatbot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Chronos bot application class. Contains main logic of the bot
 */
class ChronosBot : KoinComponent {

  private val internals by inject<ChronosInternals>()

  /**
   * Starts the bot
   */
  fun start(block: ChronosBot.() -> Unit): ChronosBot {
    block.invoke(this)
    CoroutineScope(Dispatchers.IO).launch {
      internals.start()
    }
    return this
  }
}
