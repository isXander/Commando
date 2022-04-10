package dev.isxander.commando.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

import kotlinx.coroutines.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/*
Taken from fabrik
thanks guys
 */

/**
 * A CoroutineScope using the "Default" dispatcher
 * of kotlinx.coroutines.
 */
val commandoCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

/**
 * Allows you to use coroutines for tasks which are commonly needed
 * when creating mods.
 *
 * @param howOften specifies how often the task should be executed
 * @param period the time (in ms) between each "round" of execution
 * @param delay the delay (in ms) for the task to start
 * @param task the task which should be executed
 *
 * @return the CoroutineTask
 */
inline fun coroutineTask(
    howOften: Long = 1,
    period: Duration = 0.milliseconds,
    delay: Duration = 0.milliseconds,
    crossinline task: suspend CoroutineScope.(task: CoroutineTask) -> Unit
): Job {
    val coroutineTask = CoroutineTask(howOften)
    return commandoCoroutineScope.launch {
        delay(delay)
        for (i in 1..howOften) {
            coroutineTask.round = i

            task.invoke(this, coroutineTask)

            if (!isActive) break

            if (i < howOften)
                delay(period)
        }
    }
}

class CoroutineTask(
    private val howOften: Long,
    /**
     * The current round.
     *
     * Counts up from 1 to the given value of howOften (inclusive).
     */
    var round: Long = 1,
) {
    /**
     * The current round.
     *
     * Counts up from 0 to the given value of howOften (exclusive).
     */
    val roundFromZero get() = round - 1

    /**
     * Counts down to 1, starting from the given value of howOften (inclusive).
     */
    val counterDownToOne get() = (howOften + 1) - round
    /**
     * Counts down to 0, starting from the given value of howOften (exclusive).
     */
    val counterDownToZero get() = howOften - round
}
