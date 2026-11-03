package com.roni.gymcraft.engine.content

import com.roni.gymcraft.engine.model.RaceId

/** Who plays whom in a new game (game-design.md §7): the Outpost against the Brood until more races are playable. */
data class Matchup(val player: RaceId, val enemy: RaceId)

val DefaultMatchup = Matchup(player = Outpost.id, enemy = Brood.id)
