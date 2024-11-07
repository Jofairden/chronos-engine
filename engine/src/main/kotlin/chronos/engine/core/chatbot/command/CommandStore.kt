package chronos.engine.core.chatbot.command

import chronos.engine.chatbot.CommandExecution
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class CommandStore : KoinComponent {
  /**
   * Returns all the registered commands
   */
  val allCommands: List<BotCommand> = getKoin().getAll<BotCommand>()

  /**
   * Returns all executions for commands
   */
  val allExecutions: List<CommandExecution> = allCommands.map { it.toExecution() }

  /**
   * Gets a specified command execution from the modules
   */
  inline fun <reified T : BotCommand> getCommandExecution() = getCommand<T>().toExecution()

  /**
   * Gets a specified command from the modules
   */
  inline fun <reified T : BotCommand> getCommand() = get<T>()
}
