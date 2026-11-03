package com.roni.gymcraft.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.roni.gymcraft.core.system.ProblemAction

/**
 * In place of a part that failed to load or to save (from Blocks 3.5.0). The rest of the screen keeps working.
 * Same card everywhere; the screen decides where its part is.
 */
@Composable
fun ErrorCard(onAction: (ProblemAction) -> Unit, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    Card(
        modifier = modifier.fillMaxWidth().testTag("error_card"),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.error),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Something went wrong", style = MaterialTheme.typography.titleSmall, color = colors.onSurface)
            Text(
                "This part couldn't load or save. Restarting the app usually fixes it. If it keeps happening, send the log.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { onAction(ProblemAction.SendLog) }, modifier = Modifier.testTag("error_send_log")) {
                    Text("Send log")
                }
                Button(onClick = { onAction(ProblemAction.Restart) }, modifier = Modifier.testTag("error_restart")) {
                    Text("Restart app")
                }
            }
        }
    }
}
