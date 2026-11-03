package com.roni.gymcraft.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.roni.gymcraft.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel

@Immutable
data class HomeState(
    val title: String = "GymCraft",
    val showSpriteTest: Boolean = BuildConfig.DEBUG,
)

sealed interface HomeAction {
    data object OpenSpriteTest : HomeAction
}

sealed interface HomeEffect {
    data object OpenSpriteTest : HomeEffect
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects: SharedFlow<HomeEffect> = _effects.asSharedFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OpenSpriteTest -> viewModelScope.launch {
                _effects.emit(HomeEffect.OpenSpriteTest)
            }
        }
    }
}

@Composable
fun HomeRoute(
    onOpenSpriteTest: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, onOpenSpriteTest) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeEffect.OpenSpriteTest -> onOpenSpriteTest()
            }
        }
    }

    HomeScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = state.title, style = MaterialTheme.typography.displayMedium)
        if (state.showSpriteTest) {
            Button(onClick = { onAction(HomeAction.OpenSpriteTest) }) {
                Text("Sprite test")
            }
        }
    }
}
