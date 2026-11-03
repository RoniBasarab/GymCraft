package com.roni.gymcraft.core.system

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.roni.gymcraft.MainActivity
import com.roni.gymcraft.core.logging.AppLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The error card's ways out (from Blocks 3.5.0): restart the app, share the log. Starts activities from the
 * application context, so ViewModels never hold a Context. Talks to: [AppLogger].
 */
@Singleton
class AppRescue @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logger: AppLogger,
) : Rescue {
    override fun handle(action: ProblemAction) = when (action) {
        ProblemAction.Restart -> restart()
        ProblemAction.SendLog -> shareLog()
    }

    /** A fresh MainActivity, then this process exits, so the game loads from disk again. */
    private fun restart() {
        logger.info("restart requested")
        context.startActivity(
            Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK),
        )
        Runtime.getRuntime().exit(0)
    }

    private fun shareLog() = share(logger.shareFile(), "Share log")

    private fun share(file: File, title: String) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.logs", file)
        val send = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_STREAM, uri)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(send, title).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
