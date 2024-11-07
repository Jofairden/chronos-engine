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
  fun start(args: Array<String>): ChronosBot {
    CoroutineScope(Dispatchers.IO).launch {
      internals.start(args)
    }
    return this
  }
}
