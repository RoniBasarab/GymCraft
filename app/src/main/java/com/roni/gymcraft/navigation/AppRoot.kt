package com.roni.gymcraft.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.roni.gymcraft.BuildConfig
import com.roni.gymcraft.feature.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object HomeKey : NavKey

@Serializable
data object SpriteTestKey : NavKey

@Composable
fun AppRoot() {
    val backStack = rememberNavBackStack(HomeKey)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<HomeKey> {
                HomeRoute(onOpenSpriteTest = { backStack.add(SpriteTestKey) })
            }
            if (BuildConfig.DEBUG) {
                entry<SpriteTestKey> {
                    SpriteTestPlaceholder()
                }
            }
        },
    )
}

/** Placeholder until slice 2 brings the real sprite test. */
@Composable
private fun SpriteTestPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Sprite test")
    }
}
