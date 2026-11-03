package com.roni.gymcraft.core.system

import com.roni.gymcraft.core.logging.AppLogger
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * How a failure behaves (architecture.md §9). Release builds are quiet: the failure is logged, the app carries on and
 * the screen shows its error card. Debug builds are [loud]: the failure is rethrown, so it crashes and gets fixed.
 * Provided once in `AppModule` from `BuildConfig.DEBUG`; tests pass `FailurePolicy(loud = false)`.
 */
data class FailurePolicy(val loud: Boolean)

/**
 * The one place a failure goes (from Blocks 3.5.0). Talks to: [AppLogger].
 *
 * Cancellation is never a failure: a coroutine cancelled because its screen went away just stops. A
 * [CancellationException] thrown while the coroutine is still active (a `withTimeout` that ran out) is a real failure.
 */
@Singleton
class Failures @Inject constructor(
    private val logger: AppLogger,
    private val policy: FailurePolicy,
) {
    /** Logs [error] as a failure of [where]. A loud build then rethrows it. */
    fun report(where: String, error: Throwable) {
        logger.error("failed: $where", error)
        if (policy.loud) throw error
    }

    /** `scope.launch`, except that a failure inside [block] calls [onFailure] and is [report]ed instead of crashing. */
    fun launch(
        scope: CoroutineScope,
        where: String,
        onFailure: () -> Unit = {},
        block: suspend CoroutineScope.() -> Unit,
    ): Job = scope.launch {
        try {
            block()
        } catch (e: Exception) {
            if (e is CancellationException) currentCoroutineContext().ensureActive()
            onFailure()
            report(where, e)
        }
    }

    /** Runs [block]; a failure is [report]ed and gives null. */
    suspend fun <T> attempt(where: String, block: suspend () -> T): T? =
        try {
            block()
        } catch (e: Exception) {
            if (e is CancellationException) currentCoroutineContext().ensureActive()
            report(where, e)
            null
        }
}

/** A flow whose failure doesn't kill its collector: [onFailure], reported, and the flow completes. Last before collect. */
fun <T> Flow<T>.reportFailures(failures: Failures, where: String, onFailure: () -> Unit = {}): Flow<T> =
    catch { e ->
        if (e is CancellationException) currentCoroutineContext().ensureActive()
        onFailure()
        failures.report(where, e)
    }
