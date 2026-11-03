package com.roni.gymcraft.core.system

/** What the error card offers. A screen wraps it in its own Action (`HomeAction.Problem(ProblemAction)`). */
sealed interface ProblemAction {
    data object Restart : ProblemAction
    data object SendLog : ProblemAction
}

/** The ways out when something failed: restart the app, share the log. `AppRescue` is the Android one. */
fun interface Rescue {
    fun handle(action: ProblemAction)
}

/** Debug tools on screen (the sprite test, time travel): `BuildConfig.DEBUG`, provided once in `AppModule`. */
data class DebugTools(val enabled: Boolean)
