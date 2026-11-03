package com.roni.gymcraft.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.roni.gymcraft.designsystem.ErrorCard
import com.roni.gymcraft.designsystem.GymSheet
import com.roni.gymcraft.designsystem.HoldButton
import com.roni.gymcraft.designsystem.sprites.SpriteImage
import com.roni.gymcraft.designsystem.sprites.content.SpriteIndex
import kotlinx.coroutines.delay

/** The only composable that knows [HomeViewModel] (architecture.md §6). Keep the navigation parameter AppRoot passes. */
@Composable
fun HomeRoute(openSpriteTest: () -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val open by rememberUpdatedState(openSpriteTest)
    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeEffect.OpenSpriteTest -> open()
            }
        }
    }
    LifecycleResumeEffect(viewModel) {
        viewModel.onAction(HomeAction.Resumed)
        onPauseOrDispose { }
    }
    HomeScreen(state, viewModel::onAction)
}

@Composable
fun HomeScreen(state: HomeState, onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TopBar(state, onAction)
        if (state.problem) ErrorCard(onAction = { onAction(HomeAction.Problem(it)) })
        when (val phase = state.phase) {
            HomePhase.Loading -> Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is HomePhase.TooNew -> Notice(
                "This save is from a newer GymCraft (save version ${phase.version}). Update the app. Nothing was changed.",
            )
            HomePhase.Ready -> state.game?.let { Base(it, state.reply, onAction) }
        }
    }
    state.sheet?.let { BuildingSheetView(it, onAction) }
}

@Composable
private fun TopBar(state: HomeState, onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    val game = state.game
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("${game?.supplies ?: 0}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = colors.primary)
        Text(" supplies", style = MaterialTheme.typography.titleMedium, color = colors.onSurfaceVariant)
        Spacer(Modifier.weight(1f))
        if (state.debugTools && game != null) Text(game.time, style = MaterialTheme.typography.labelMedium, color = colors.onSurfaceVariant)
        Box {
            TextButton(onClick = { onAction(HomeAction.ShowMenu(true)) }, modifier = Modifier.testTag("home_menu")) {
                Text("⋮", style = MaterialTheme.typography.titleLarge)
            }
            DropdownMenu(expanded = state.menuOpen, onDismissRequest = { onAction(HomeAction.ShowMenu(false)) }) {
                if (game?.onLeave == true) {
                    DropdownMenuItem(text = { Text("I'm back") }, onClick = { onAction(HomeAction.EndLeave) })
                } else {
                    DropdownMenuItem(text = { Text("Going on leave") }, onClick = { onAction(HomeAction.StartLeave) })
                }
                if (state.debugTools) {
                    DropdownMenuItem(text = { Text("Sprite test") }, onClick = { onAction(HomeAction.OpenSpriteTest) })
                    DropdownMenuItem(text = { Text("Debug: time +1 hour") }, onClick = { onAction(HomeAction.TimeForward(60)) })
                    DropdownMenuItem(text = { Text("Debug: time +1 day") }, onClick = { onAction(HomeAction.TimeForward(24 * 60)) })
                    DropdownMenuItem(text = { Text("Debug: back to real time") }, onClick = { onAction(HomeAction.TimeReset) })
                }
            }
        }
    }
}

@Composable
private fun Base(game: GameUi, reply: String?, onAction: (HomeAction) -> Unit) {
    if (game.onLeave) LeaveBanner(onAction)
    BaseView(game, onAction)
    Sergeant(game, reply, onAction)
    game.forecast?.let { ForecastCard(it) }
    if (game.away.isNotEmpty()) AwayCard(game.away, onAction)
    HomeCardRow(game.card, onAction)
    HoldButton(
        text = if (game.wentToday) "Logged today. Rest up, recruit." else "HOLD: I WENT TO THE GYM",
        enabled = !game.wentToday,
        onConfirmed = { onAction(HomeAction.Went) },
    )
}

@Composable
private fun LeaveBanner(onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Card(colors = CardDefaults.cardColors(containerColor = colors.primary.copy(alpha = 0.18f))) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("On leave", style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
                Text("No raids, no boss. Nothing decays.", style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
            }
            Button(onClick = { onAction(HomeAction.EndLeave) }) { Text("I'm back") }
        }
    }
}

/** The drill sergeant: his portrait and a bubble with his line, or his answer to the last tap (tap it away). */
@Composable
private fun Sergeant(game: GameUi, reply: String?, onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    val text = reply ?: game.sergeant
    var talking by remember { mutableStateOf(false) }   // animation state: the mouth moves for a moment per new line
    LaunchedEffect(text) {
        talking = true
        delay(1_500)
        talking = false
    }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpriteImage(SpriteIndex.portrait(game.race), Modifier.size(72.dp), talking = talking)
        Box(
            Modifier
                .weight(1f)
                .background(colors.surface, RoundedCornerShape(14.dp))
                .then(if (reply != null) Modifier.clickable { onAction(HomeAction.DismissReply) } else Modifier)
                .padding(12.dp)
                .testTag("sergeant_line"),
        ) {
            Text(text, style = MaterialTheme.typography.bodyLarge, color = colors.onSurface)
        }
    }
}

@Composable
private fun ForecastCard(forecast: ForecastUi) {
    val colors = MaterialTheme.colorScheme
    Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(forecast.title, style = MaterialTheme.typography.labelLarge, color = colors.primary)
            Text(forecast.wave, style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
            Text("Win chance as things stand: ${forecast.chance}%", style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
        }
    }
}

/** "While you were away": one line per battle fought since the last look. Watch buttons come with the battle player. */
@Composable
private fun AwayCard(lines: List<String>, onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Card(colors = CardDefaults.cardColors(containerColor = colors.surface), modifier = Modifier.testTag("away_card")) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("While you were away", style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
            lines.forEach { Text(it, style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant) }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { onAction(HomeAction.DismissAway) }) { Text("Got it") }
            }
        }
    }
}

@Composable
private fun HomeCardRow(card: HomeCardUi, onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Today's home card", style = MaterialTheme.typography.labelLarge, color = colors.onSurfaceVariant)
                Text(card.text, style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
            }
            OutlinedButton(onClick = { onAction(HomeAction.CardDone) }, enabled = !card.done, modifier = Modifier.testTag("card_done")) {
                Text(if (card.done) "Done" else "Done +10")
            }
        }
    }
}

@Composable
private fun Notice(text: String) {
    val colors = MaterialTheme.colorScheme
    Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
        Text(text, Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge, color = colors.onSurface)
    }
}

/** A production building: train its units (+/− count) and upgrade it. Closed by a swipe down or a tap outside. */
@Composable
private fun BuildingSheetView(sheet: BuildingSheet, onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    GymSheet(onDismiss = { onAction(HomeAction.CloseSheet) }) {
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp).navigationBarsPadding().padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SpriteImage(sheet.art, Modifier.size(width = 96.dp, height = 72.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(sheet.name, style = MaterialTheme.typography.titleLarge, color = colors.onSurface)
                    Text("Level ${sheet.level} of ${sheet.maxLevel}", style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
                }
            }
            if (sheet.occupied) {
                Text("Occupied. One gym session and we take it back.", style = MaterialTheme.typography.bodyMedium, color = colors.error)
            }
            sheet.units.forEach { UnitRow(it, onAction) }
            if (sheet.upgradeCost != null) {
                Button(onClick = { onAction(HomeAction.Upgrade) }, enabled = sheet.canUpgrade, modifier = Modifier.fillMaxWidth().testTag("upgrade")) {
                    Text("Upgrade to level ${sheet.level + 1} · ${sheet.upgradeCost}")
                }
            } else {
                Text("Fully upgraded.", style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun UnitRow(unit: SheetUnit, onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Row(verticalAlignment = Alignment.CenterVertically) {
        SpriteImage(unit.art, Modifier.size(52.dp).alpha(if (unit.locked) 0.35f else 1f))
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(unit.name, style = MaterialTheme.typography.titleSmall, color = if (unit.locked) colors.onSurfaceVariant else colors.onSurface)
            Text(
                if (unit.locked) "Needs level ${unit.tier}" else "${unit.cost} each · ${unit.ready} ready",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant,
            )
        }
        if (!unit.locked) {
            TextButton(onClick = { onAction(HomeAction.Pick(unit.id, unit.count - 1)) }, enabled = unit.count > 1) { Text("−") }
            Text("${unit.count}", style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
            TextButton(onClick = { onAction(HomeAction.Pick(unit.id, unit.count + 1)) }, enabled = unit.count < SheetUnit.MAX) { Text("+") }
            Button(onClick = { onAction(HomeAction.Train(unit.id)) }, enabled = unit.canTrain) { Text("Train · ${unit.total}") }
        }
    }
}
