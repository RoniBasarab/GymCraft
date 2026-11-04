package com.roni.gymcraft.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.roni.gymcraft.designsystem.ErrorCard
import com.roni.gymcraft.designsystem.GymSheet
import com.roni.gymcraft.designsystem.LiftButton
import com.roni.gymcraft.designsystem.sprites.SpriteIcon
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

/**
 * Home is the base, full screen (game-design.md §12): a sky strip with protein and the menu, the scene, and a ground
 * strip with the sergeant, "While you were away", the boss, the home card and the lift. The strips use the scene's
 * own sky and ground colors, so it reads as one picture.
 */
@Composable
fun HomeScreen(state: HomeState, onAction: (HomeAction) -> Unit) {
    Column(Modifier.fillMaxSize().background(BaseGround)) {
        Column(
            Modifier.fillMaxWidth().background(BaseSky).statusBarsPadding().padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TopBar(state, onAction)
            if (state.problem) ErrorCard(onAction = { onAction(HomeAction.Problem(it)) })
            if (state.game?.onLeave == true) LeaveBanner(onAction)
        }
        val game = state.game
        when (val phase = state.phase) {
            HomePhase.Loading -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is HomePhase.TooNew -> Box(Modifier.weight(1f).fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                Notice("This save is from a newer GymCraft (save version ${phase.version}). Update the app. Nothing was changed.")
            }
            HomePhase.Ready -> if (game != null) {
                BaseView(game, state.cheers, onAction, Modifier.weight(1f).fillMaxWidth())
                Ground(game, state.reply, onAction)
            } else {
                Spacer(Modifier.weight(1f))
            }
        }
    }
    state.sheet?.let { BuildingSheetView(it, onAction) }
}

@Composable
private fun TopBar(state: HomeState, onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    val game = state.game
    Row(Modifier.fillMaxWidth().padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        SpriteIcon(SpriteIndex.PROTEIN, Modifier.size(30.dp))
        Spacer(Modifier.width(6.dp))
        Text("${game?.protein ?: 0}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = colors.primary)
        Text(" protein", style = MaterialTheme.typography.titleSmall, color = colors.onSurfaceVariant)
        Spacer(Modifier.weight(1f))
        if (state.debugTools && game != null) Text(game.time, style = MaterialTheme.typography.labelMedium, color = colors.onSurfaceVariant)
        Box {
            TextButton(onClick = { onAction(HomeAction.ShowMenu(true)) }, modifier = Modifier.testTag("home_menu")) {
                Text("⋮", style = MaterialTheme.typography.titleLarge, color = colors.onSurface)
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
private fun LeaveBanner(onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Row(
        Modifier.fillMaxWidth().background(colors.primary.copy(alpha = 0.18f), RoundedCornerShape(12.dp)).padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("On leave. No raids, no boss.", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = colors.onSurface)
        TextButton(onClick = { onAction(HomeAction.EndLeave) }) { Text("I'm back") }
    }
}

/** The ground strip under the scene. */
@Composable
private fun Ground(game: GameUi, reply: String?, onAction: (HomeAction) -> Unit) {
    Column(
        Modifier.fillMaxWidth().navigationBarsPadding().padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Sergeant(game, reply, onAction)
        if (game.away.isNotEmpty()) AwayCard(game.away, onAction)
        game.forecast?.let { BossRow(it) }
        HomeCardRow(game, onAction)
        LiftButton(
            text = if (game.wentToday) "Logged today. Rest up, recruit." else "HOLD: I WENT TO THE GYM",
            enabled = !game.wentToday,
            onConfirmed = { onAction(HomeAction.Went) },
        )
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
        SpriteImage(SpriteIndex.portrait(game.race), Modifier.size(56.dp), talking = talking)
        Box(
            Modifier
                .weight(1f)
                .background(BaseSky, RoundedCornerShape(12.dp))
                .then(if (reply != null) Modifier.clickable { onAction(HomeAction.DismissReply) } else Modifier)
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .testTag("sergeant_line"),
        ) {
            Text(text, style = MaterialTheme.typography.bodyMedium, color = colors.onSurface)
        }
    }
}

/** One line: "BOSS  Sat 20:00 · 30 Biters and 2 Brutes   62%". */
@Composable
private fun BossRow(forecast: ForecastUi) {
    val colors = MaterialTheme.colorScheme
    val kind = forecast.title.substringBefore(" · ").uppercase()
    val time = forecast.title.substringAfter(" · ")
    StripRow {
        Text(kind, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = colors.primary)
        Spacer(Modifier.width(8.dp))
        Text("$time · ${forecast.wave}", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = colors.onSurface,
            maxLines = 2, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.width(8.dp))
        Text("${forecast.chance}%", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = colors.onSurface)
    }
}

/** One line: the drill recruit doing today's move, its text, and +10 protein. */
@Composable
private fun HomeCardRow(game: GameUi, onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    val card = game.card
    StripRow(vertical = 2.dp) {
        SpriteImage(SpriteIndex.drill(game.race, card.move), Modifier.size(40.dp), hold = card.hold)
        Spacer(Modifier.width(8.dp))
        Text(card.text, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = colors.onSurface)
        OutlinedButton(
            onClick = { onAction(HomeAction.CardDone) },
            enabled = !card.done,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.testTag("card_done"),
        ) {
            if (card.done) Text("Done") else Price("+10")
        }
    }
}

@Composable
private fun StripRow(vertical: Dp = 8.dp, content: @Composable RowScope.() -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(BaseSky.copy(alpha = 0.85f), RoundedCornerShape(10.dp)).padding(horizontal = 10.dp, vertical = vertical),
        verticalAlignment = Alignment.CenterVertically,
    ) { content() }
}

/** An amount of protein: "150" with the tub after it. */
@Composable
private fun Price(amount: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(amount)
        Spacer(Modifier.width(4.dp))
        SpriteIcon(SpriteIndex.PROTEIN, Modifier.size(16.dp))
    }
}

/** "While you were away": one line per battle fought since the last look. Watch buttons come with the battle player. */
@Composable
private fun AwayCard(lines: List<String>, onAction: (HomeAction) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Column(
        Modifier.fillMaxWidth().background(BaseSky, RoundedCornerShape(10.dp)).padding(horizontal = 10.dp, vertical = 6.dp).testTag("away_card"),
    ) {
        Text("While you were away", style = MaterialTheme.typography.labelLarge, color = colors.primary)
        lines.forEach { Text(it, style = MaterialTheme.typography.bodySmall, color = colors.onSurface) }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { onAction(HomeAction.DismissAway) }) { Text("Got it") }
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
                Column(Modifier.weight(1f)) {
                    Text("${sheet.name} ${sheet.level}", style = MaterialTheme.typography.titleLarge, color = colors.onSurface)
                    Text("Level ${sheet.level} of ${sheet.maxLevel}", style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
                }
            }
            if (sheet.occupied) {
                Text("Occupied. One gym session and we take it back.", style = MaterialTheme.typography.bodyMedium, color = colors.error)
            }
            sheet.units.forEach { UnitRow(it, onAction) }
            if (sheet.upgradeCost != null) {
                Button(onClick = { onAction(HomeAction.Upgrade) }, enabled = sheet.canUpgrade, modifier = Modifier.fillMaxWidth().testTag("upgrade")) {
                    Text("Upgrade to ${sheet.name} ${sheet.level + 1} · ")
                    Price("${sheet.upgradeCost}")
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
            if (unit.locked) {
                Text("Needs level ${unit.tier}", style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${unit.cost}", style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
                    Spacer(Modifier.width(3.dp))
                    SpriteIcon(SpriteIndex.PROTEIN, Modifier.size(12.dp))
                    Text(" each · ${unit.ready} ready", style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
                }
            }
        }
        if (!unit.locked) {
            TextButton(onClick = { onAction(HomeAction.Pick(unit.id, unit.count - 1)) }, enabled = unit.count > 1) { Text("−") }
            Text("${unit.count}", style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
            TextButton(onClick = { onAction(HomeAction.Pick(unit.id, unit.count + 1)) }, enabled = unit.count < SheetUnit.MAX) { Text("+") }
            Button(
                onClick = { onAction(HomeAction.Train(unit.id)) },
                enabled = unit.canTrain,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            ) {
                Text("Train ")
                Price("${unit.total}")
            }
        }
    }
}
