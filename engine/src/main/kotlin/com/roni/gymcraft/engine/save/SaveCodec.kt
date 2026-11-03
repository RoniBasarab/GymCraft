package com.roni.gymcraft.engine.save

import com.roni.gymcraft.engine.battle.BattleInput
import com.roni.gymcraft.engine.battle.SquadSpec
import com.roni.gymcraft.engine.model.BattleKind
import com.roni.gymcraft.engine.model.BattleOutcome
import com.roni.gymcraft.engine.model.BuildingId
import com.roni.gymcraft.engine.model.BuildingState
import com.roni.gymcraft.engine.model.GameState
import com.roni.gymcraft.engine.model.LeavePeriod
import com.roni.gymcraft.engine.model.RaceId
import com.roni.gymcraft.engine.model.UnitId
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.put

/** What the save file holds: the game, plus how many battles the player has already been shown. */
data class SaveFile(val state: GameState, val seenBattles: Int = 0)

/**
 * The save file's JSON (architecture.md §7), written field by field so the format is visible and stable: renaming a
 * Kotlin property never breaks a save. Dates are ISO strings ("2026-11-01", "2026-11-07T20:00").
 *
 * [VERSION] goes up when the format changes; [migrate] brings an older file up to date step by step, each step with
 * a test. A file newer than this app is refused (never overwritten), and anything unreadable is reported as
 * [Decoded.Broken] so the app can fall back to the backup.
 */
object SaveCodec {
    const val VERSION = 1

    sealed interface Decoded {
        data class Ok(val file: SaveFile, val migratedFrom: Int? = null) : Decoded
        data class TooNew(val version: Int) : Decoded
        data class Broken(val reason: String) : Decoded
    }

    private val json = Json { prettyPrint = false }

    fun encode(file: SaveFile): String = json.encodeToString(JsonElement.serializer(), buildJsonObject {
        put("version", VERSION)
        put("seenBattles", file.seenBattles)
        put("state", state(file.state))
    })

    fun decode(text: String): Decoded = try {
        val root = json.parseToJsonElement(text).jsonObject
        val version = root.getValue("version").jsonPrimitive.int
        when {
            version > VERSION -> Decoded.TooNew(version)
            version < 1 -> Decoded.Broken("version $version")
            else -> {
                val current = migrate(version, root)
                Decoded.Ok(
                    SaveFile(state(current.getValue("state").jsonObject), current["seenBattles"]?.jsonPrimitive?.int ?: 0),
                    migratedFrom = if (version < VERSION) version else null,
                )
            }
        }
    } catch (e: Exception) {
        Decoded.Broken(e.message ?: e::class.simpleName.orEmpty())
    }

    /** Brings a file written at [from] up to [VERSION]. No older versions exist yet: version 1 is the first. */
    private fun migrate(from: Int, root: JsonObject): JsonObject {
        var r = root
        var v = from
        while (v < VERSION) {
            r = when (v) {
                else -> r
            }
            v++
        }
        return r
    }

    // ---- encode ----

    private fun state(s: GameState): JsonObject = buildJsonObject {
        put("race", s.race.value)
        put("enemy", s.enemy.value)
        put("seasonStart", s.seasonStart.toString())
        put("seasonSeed", s.seasonSeed)
        put("supplies", s.supplies)
        put("buildings", buildJsonObject {
            s.buildings.forEach { (id, b) -> put(id.value, buildJsonObject { put("level", b.level); put("occupied", b.occupied) }) }
        })
        put("ready", counts(s.ready))
        put("wounded", counts(s.wounded))
        put("sessions", days(s.sessions))
        put("homeCards", days(s.homeCards))
        put("leave", buildJsonArray {
            s.leave.forEach { l -> add(buildJsonObject { put("from", l.from.toString()); put("until", l.until?.toString()) }) }
        })
        put("resolvedThrough", s.resolvedThrough.toString())
        put("battles", buildJsonArray { s.battles.forEach { add(outcome(it)) } })
    }

    private fun outcome(o: BattleOutcome): JsonObject = buildJsonObject {
        put("at", o.at.toString())
        put("kind", o.kind.name)
        put("week", o.week)
        put("won", o.won)
        put("input", buildJsonObject {
            put("defenderRace", o.input.defenderRace.value)
            put("defenders", squads(o.input.defenders))
            put("attackerRace", o.input.attackerRace.value)
            put("attackers", squads(o.input.attackers))
            put("seed", o.input.seed)
        })
        put("fallen", counts(o.fallen))
        put("occupied", o.occupied?.value)
    }

    private fun squads(list: List<SquadSpec>): JsonArray = buildJsonArray {
        list.forEach { q -> add(buildJsonObject { put("unit", q.unit.value); put("count", q.count); put("level", q.level) }) }
    }

    private fun counts(map: Map<UnitId, Int>): JsonObject = buildJsonObject { map.forEach { (k, v) -> put(k.value, v) } }

    private fun days(list: List<LocalDate>): JsonArray = buildJsonArray { list.forEach { add(JsonPrimitive(it.toString())) } }

    // ---- decode ----

    private fun state(o: JsonObject): GameState = GameState(
        race = RaceId(o.str("race")),
        enemy = RaceId(o.str("enemy")),
        seasonStart = LocalDate.parse(o.str("seasonStart")),
        seasonSeed = o.getValue("seasonSeed").jsonPrimitive.long,
        supplies = o.getValue("supplies").jsonPrimitive.int,
        buildings = o.getValue("buildings").jsonObject.entries.associate { (id, b) ->
            BuildingId(id) to BuildingState(b.jsonObject.getValue("level").jsonPrimitive.int, b.jsonObject.getValue("occupied").jsonPrimitive.boolean)
        },
        ready = counts(o.getValue("ready")),
        wounded = counts(o.getValue("wounded")),
        sessions = days(o.getValue("sessions")),
        homeCards = days(o.getValue("homeCards")),
        leave = o.getValue("leave").jsonArray.map { l ->
            val lo = l.jsonObject
            LeavePeriod(LocalDate.parse(lo.str("from")), lo.optStr("until")?.let(LocalDate::parse))
        },
        resolvedThrough = LocalDateTime.parse(o.str("resolvedThrough")),
        battles = o.getValue("battles").jsonArray.map { outcome(it.jsonObject) },
    )

    private fun outcome(o: JsonObject): BattleOutcome {
        val i = o.getValue("input").jsonObject
        return BattleOutcome(
            at = LocalDateTime.parse(o.str("at")),
            kind = BattleKind.valueOf(o.str("kind")),
            week = o.getValue("week").jsonPrimitive.int,
            won = o.getValue("won").jsonPrimitive.boolean,
            input = BattleInput(
                RaceId(i.str("defenderRace")), squads(i.getValue("defenders")),
                RaceId(i.str("attackerRace")), squads(i.getValue("attackers")),
                i.getValue("seed").jsonPrimitive.long,
            ),
            fallen = counts(o.getValue("fallen")),
            occupied = o.optStr("occupied")?.let(::BuildingId),
        )
    }

    private fun squads(e: JsonElement): List<SquadSpec> = e.jsonArray.map {
        val q = it.jsonObject
        SquadSpec(UnitId(q.str("unit")), q.getValue("count").jsonPrimitive.int, q.getValue("level").jsonPrimitive.int)
    }

    private fun counts(e: JsonElement): Map<UnitId, Int> = e.jsonObject.entries.associate { (k, v) -> UnitId(k) to v.jsonPrimitive.int }

    private fun days(e: JsonElement): List<LocalDate> = e.jsonArray.map { LocalDate.parse(it.jsonPrimitive.content) }

    private fun JsonObject.str(key: String): String = getValue(key).jsonPrimitive.content

    private fun JsonObject.optStr(key: String): String? = this[key]?.takeIf { it !is JsonNull }?.jsonPrimitive?.content
}
