package com.roni.gymcraft.feature.spritetest

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.roni.gymcraft.designsystem.GymColors
import com.roni.gymcraft.designsystem.sprites.rememberCutoutSprites
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Immutable
data class SpriteTestState(val count: Int = 200, val fps: String = "")

sealed interface SpriteTestAction {
    data class SetCount(val count: Int) : SpriteTestAction
    data class Measured(val fps: Int, val worstMs: Int) : SpriteTestAction
}

@HiltViewModel
class SpriteTestViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SpriteTestState())
    val state: StateFlow<SpriteTestState> = _state.asStateFlow()

    fun onAction(action: SpriteTestAction) {
        when (action) {
            is SpriteTestAction.SetCount -> _state.update { it.copy(count = action.count, fps = "") }
            is SpriteTestAction.Measured ->
                _state.update { it.copy(fps = "${action.fps} fps · worst frame ${action.worstMs} ms") }
        }
    }
}

@Composable
fun SpriteTestRoute(viewModel: SpriteTestViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SpriteTestScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun SpriteTestScreen(
    state: SpriteTestState,
    onAction: (SpriteTestAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Battlefield(
            count = state.count,
            onAction = onAction,
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CountButton(50, state.count, onAction)
                CountButton(200, state.count, onAction)
                CountButton(500, state.count, onAction)
            }
            Text(
                text = state.fps,
                color = GymColors.Muted,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun CountButton(count: Int, selected: Int, onAction: (SpriteTestAction) -> Unit) {
    Button(onClick = { onAction(SpriteTestAction.SetCount(count)) }) {
        Text(text = count.toString(), color = if (selected == count) GymColors.OnPrimary else GymColors.OnBackground)
    }
}

@Composable
fun Battlefield(count: Int, onAction: (SpriteTestAction) -> Unit, modifier: Modifier = Modifier) {
    val sprites = rememberCutoutSprites()                 // part bitmaps, built once
    val world = remember(count) { SpriteWorld(count) }    // FloatArrays, built once per count
    var frameNanos by remember { mutableLongStateOf(0L) }
    val action by rememberUpdatedState(onAction)

    LaunchedEffect(world) {
        val meter = FpsMeter()
        while (true) withFrameNanos { now ->
            frameNanos = now
            meter.frame(now)?.let { (fps, worst) -> action(SpriteTestAction.Measured(fps, worst)) }   // once a second
        }
    }
    Canvas(modifier.fillMaxSize()) {
        val t = frameNanos             // read only here: a new frame redraws, never recomposes
        world.step(t, size.width, size.height)
        world.draw(this, sprites, t)
    }
}
