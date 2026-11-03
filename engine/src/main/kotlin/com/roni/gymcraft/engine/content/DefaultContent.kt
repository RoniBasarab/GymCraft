package com.roni.gymcraft.engine.content

import com.roni.gymcraft.engine.model.GameContent

/** Everything that ships. The app injects this; tests may build their own [GameContent]. */
val DefaultContent = GameContent(listOf(Outpost.race, Brood.race))
