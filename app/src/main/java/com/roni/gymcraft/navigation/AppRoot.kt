package com.roni.gymcraft.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.roni.gymcraft.BuildConfig
import com.roni.gymcraft.feature.home.HomeRoute
import com.roni.gymcraft.feature.spritetest.SpriteTestRoute
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
                HomeRoute(openSpriteTest = { backStack.add(SpriteTestKey) })
            }
            if (BuildConfig.DEBUG) {
                entry<SpriteTestKey> {
                    SpriteTestRoute()
                }
            }
        },
    )
}
