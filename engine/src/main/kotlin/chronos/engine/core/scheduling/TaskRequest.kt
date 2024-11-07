package chronos.engine.core.scheduling

import arrow.optics.optics

@optics
data class TaskRequest(
  override val id: String,
  override val repeatCount: Int = 0,
  override val indefinitely: Boolean = false,
  override val initialDelay: Long = 0L,
  override val delayInMillis: Long = 0L,
  override val task: (suspend () -> Unit)? = null,
  override val data: Map<String, Any> = mapOf()
) : ITaskRequest
