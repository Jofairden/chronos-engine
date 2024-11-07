package chronos.engine.core.scheduling

import arrow.optics.optics

@optics
data class ScheduledTaskRequest(
  override val id: String,
  val name: String,
  override val repeatCount: Int = 0,
  override val indefinitely: Boolean = false,
  override val initialDelay: Long = 0,
  override val delayInMillis: Long = 0,
  override val task: (suspend () -> Unit)? = null,
) : TaskRequest(id, repeatCount, indefinitely, initialDelay, delayInMillis, task)
