package chronos.engine.core.services

import arrow.optics.optics
import chronos.engine.core.dsl.asLoggable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

@Suppress("UnnecessaryAbstractClass")
/**
 * A service for scheduling tasks to be executed asynchronously.
 *
 * @param scope The [CoroutineScope] to execute the tasks.
 */
abstract class SchedulerService(scope: CoroutineScope) {
    interface ITaskRequest {
        val id: String
        val repeatCount: Int
        val indefinitely: Boolean
        val initialDelay: Long
        val delayInMillis: Long
        val task: (suspend () -> Unit)?
    }

    @optics
    open class TaskRequest(
        override val id: String,
        override val repeatCount: Int = 0,
        override val indefinitely: Boolean = false,
        override val initialDelay: Long = 0L,
        override val delayInMillis: Long = 0L,
        override val task: (suspend () -> Unit)? = null,
    ) : ITaskRequest

    private val tasksFlow = MutableSharedFlow<TaskRequest>()
    private val jobs = ConcurrentHashMap<String, Job>()

    init {
        scope.launch(Dispatchers.Default) {
            tasksFlow.collect { taskRequest ->
                if (taskRequest.task == null) {
                    asLoggable("Cannot start task request if internal task is empty") {
                        error()
                    }
                    return@collect
                }

                val job =
                    launch {
                        if (taskRequest.initialDelay > 0) delay(taskRequest.initialDelay)

                        when {
                            taskRequest.indefinitely -> while (isActive) {
                                taskRequest.task?.let { it() }
                                if (taskRequest.delayInMillis > 0) delay(taskRequest.delayInMillis)
                            }

                            taskRequest.repeatCount > 0 ->
                                repeat(taskRequest.repeatCount) {
                                    taskRequest.task?.let { it() }
                                    if (taskRequest.delayInMillis > 0) delay(taskRequest.delayInMillis)
                                }

                            else -> taskRequest.task?.let { it() }
                        }
                    }
                jobs[taskRequest.id] = job
                job.invokeOnCompletion { jobs.remove(taskRequest.id) }
            }
        }
    }

    /**
     * Schedules the task request by emitting it to the tasksFlow.
     *
     * @receiver The task request to be scheduled.
     */
    suspend fun TaskRequest.schedule() = tasksFlow.emit(this)

    /**
     * Schedules a task based on the provided TaskRequest.
     *
     * @param req The TaskRequest object containing the details of the task to be scheduled.
     * @throws Exception if an error occurs while scheduling the task.
     */
    suspend fun scheduleTask(req: TaskRequest) = req.schedule()

    /**
     * Cancels the task with the specified ID.
     *
     * @param id the ID of the task to cancel
     */
    suspend fun cancelTask(id: String) {
        jobs[id]?.cancelAndJoin()
    }
}
