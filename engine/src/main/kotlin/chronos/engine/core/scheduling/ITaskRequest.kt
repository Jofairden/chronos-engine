package chronos.engine.core.scheduling

interface ITaskRequest {
  val id: String
  val repeatCount: Int
  val indefinitely: Boolean
  val initialDelay: Long
  val delayInMillis: Long
  val task: (suspend () -> Unit)?
}
