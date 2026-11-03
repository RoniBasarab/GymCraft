# GymCraft: architecture

Decided by Roni with the planning agent, 3 Nov 2026. **Binding.** If something seems to need another way, stop and say so; never improvise. Roni decides, after pros and cons.

## 1. Principles

1. **Rules are pure Kotlin.** Economy, waves, forecast, battle simulation and race definitions live in `:engine`, which has no Android at all. The compiler enforces it.
2. **Races are data.** Game code never names a race. A new race is a new definition file plus its art.
3. **Compose shows, never decides.** Composables render state and forward actions; ViewModels bridge; `:engine` decides.
4. **Performance first.** Battle playback and idle animations never recompose a screen per frame, and nothing is allocated per frame.
5. **Soft in release, loud in debug.** Failures are logged and shown as an error card in release, and crash in debug (§9).
6. **No over-engineering.** No use-case layer, no reducers. State + `onAction`, one ViewModel per screen.

## 2. Stack

| Concern | Choice |
|---|---|
| Language / UI | Kotlin, Jetpack Compose, Material 3 (dark), Canvas for the base and the battle |
| SDK | minSdk 29, target / compile 37 |
| Modules | `:app` (Android) + `:engine` (pure Kotlin JVM library) |
| DI | Hilt (in `:app` only) |
| Navigation | Navigation 3 |
| Save | One versioned JSON save file (kotlinx.serialization), written atomically |
| Time | `java.time` in the engine (`LocalDate`, `DayOfWeek`, epoch millis) and an injected clock. No kotlinx-datetime, no experimental `kotlin.time` |
| Collections in state | Plain `List` + the Compose stability config (no immutable-collections library) |
| Build | The same versions as Blocks (they're in the Gradle cache), version catalog, build cache, `codeRules` |

## 3. Modules and packages

```
:engine   com.roni.gymcraft.engine
  model/      ids (RaceId, UnitId, BuildingId, TraitId as value classes), GameState, Squad
  content/    race definitions: Outpost.kt, Brood.kt, Races.kt (the registry). The only place race names appear
  economy/    supplies, costs, sessions, comeback rally, home cards
  waves/      the weekly boss and daily raids from a seed + the week; the forecast
  battle/     BattleSim (fixed step, seeded random) → BattleRecord; counters
  season/     seasons, perks
  save/       GameSave (versioned), migrations
:app      com.roni.gymcraft
  core/       clock, save store, notifications, Failures, logging, di
  designsystem/  theme, components, sprite drawing, experimental wrappers
  feature/    base/, battle/, forecast/, leave/, settings/ ... (one package per screen)
  navigation/
```

## 4. Races are data

```kotlin
data class RaceDef(val id: RaceId, val name: String, val hq: HqDef, val buildings: List<BuildingDef>)
data class BuildingDef(val id: BuildingId, val name: String, val levels: List<LevelDef>)   // level n unlocks units[n-1]
data class UnitDef(val id: UnitId, val name: String, val cost: Int, val tier: Int, val stats: Stats,
                   val trait: TraitId, val bonus: Map<TraitId, Double> = emptyMap(), val splash: Double = 0.0,
                   val heal: Double = 0.0, val art: ArtId)
```

- The engine only ever works with these definitions. `codeRules` fails the build when a race name appears outside a `content/` folder.
- Traits are ids too (`light`, `armored`, `swarm`), so a new race can bring a new one.
- **Art is looked up by `ArtId`** in `:app`'s `designsystem/sprites`. A missing sprite draws a labelled placeholder, never a crash.

## 5. Battles: simulate, record, play back

- **`BattleSim.run(attack, defence, seed): BattleRecord`**:
  - pure;
  - a fixed time step (e.g. 10 ticks a second);
  - squads, not single units: a squad's health is its count × the unit's health, and its count drops as health falls;
  - deterministic: the same inputs and seed give the same record. Tests rely on it.
- **`BattleRecord`**: per tick, each squad's position, count and events (shot, hit, heal, fell). It's small, kept in the save for the last battles, and drives **Rewatch**.
- A battle is resolved **when it's due**, whether or not the app is open. Opening the app (or the notification job) runs whatever battles fell due since the last run, in order.
- **The player (`feature/battle`):**
  - the ViewModel gives it a finished `BattleRecord`;
  - the playback position (frame clock, speed) is **local animation state** in the composable, as `remember` is allowed for animation state;
  - drawing happens in a `Canvas`/`drawWithCache`, reading the frame only in the draw phase;
  - no objects created per frame.

## 6. Screens: MVI (the Blocks pattern)

Per screen:
- an `@Immutable` State;
- a sealed Action;
- a sealed Effect (one-off: navigation, a snackbar);
- an `@HiltViewModel` with injected managers, a single `onAction`, state changed only via `_state.update { it.copy(...) }`, and effects through `MutableSharedFlow`.

The Route is the only composable that knows the ViewModel; every other composable takes (state, `onAction`), never a list of callback parameters. Design-system components (buttons, sheets, cards) take plain parameters and slots, like Material's own. No public ViewModel functions besides `onAction`. No global or static stores.

## 7. Save

- **One file:** `files/save.json` holds `GameSave(version, state, history, battles)`.
- **Written atomically:** write `save.json.tmp`, then rename; the previous save is kept as `save.json.bak`.
- **Read:** a newer version number refuses with the error card; an older one is migrated by `save/` migrations, each with a test.
- **A save that can't be read** falls back to `.bak`. If that fails too, both are set aside (renamed, never deleted), the error card shows, and the app starts fresh.
- **Backup and restore** from Settings share the same JSON.

## 8. Time and schedules

- An injected clock everywhere. Debug builds get a debug clock you can move, like Blocks' debug time.
- **Battles fall due by date and time** (Saturday 20:00 boss, other evenings' raids), computed by the engine from the save and now.
- **Notifications** (the mid-week forecast, results) come from an inexact scheduled job. No exact alarms; nothing time-critical.

## 9. Failures

- `Failures` (from Blocks 3.5.0): `report(where, error)`, `launch(scope, where, onFailure) { }`, `Flow.reportFailures(...)`, and `FailurePolicy(loud = BuildConfig.DEBUG)`.
- Release logs and shows an `ErrorCard` ("Something went wrong", **Restart app**, **Send log**) in place of the broken part. Debug crashes.
- **No `!!`.** `codeRules` fails the build on `!!`, `GlobalScope`, `runBlocking`, experimental opt-ins outside `designsystem/`, and race names outside `content/`.

## 10. Testing

- **`:engine`:** plain JVM JUnit, fast. Every rule gets tests; the planning agent ships the tricky ones (simulation, forecast, economy) as verified code.
- **`:app`:** ViewModel tests driven through `onAction` (Robolectric only where Android is unavoidable).
- **No Compose UI tests and no screenshot tests**, unless a task names one for behavior nothing else can check.
- **A failing test means the code is wrong.** A test's expectations change only when the task says so.

## 11. Performance rules for drawing

- A frame clock (`withFrameNanos`) drives playback. The frame value is read only inside draw lambdas, so a frame redraws without recomposing.
- Sprites are drawn from bitmaps prepared once (cutout parts), with translate / rotate / scale per part.
- Positions live in preallocated arrays; no lists, lambdas or objects created per frame.
- Idle animations stop when the screen isn't visible.
