# GymCraft codemap (0.2.0)

One line per source file.

## :engine — model
- `engine/src/main/kotlin/com/roni/gymcraft/engine/model/Ids.kt` — value-class ids (RaceId, BuildingId, UnitId, TraitId, ArtId) for races-as-data.
- `engine/src/main/kotlin/com/roni/gymcraft/engine/model/Defs.kt` — `Stats`, `UnitDef`, `BuildingDef`, `HqDef`, `RaceDef`, `GameContent`: races as data.
- `engine/src/main/kotlin/com/roni/gymcraft/engine/model/GameState.kt` — the whole game state, `BuildingState`, `LeavePeriod`, `BattleOutcome`.
- `engine/src/test/kotlin/com/roni/gymcraft/engine/model/IdsTest.kt` — `idsCompareByValue`.

## :engine — content (the only place race names appear)
- `engine/src/main/kotlin/com/roni/gymcraft/engine/content/Traits.kt` — the trait ids and their definitions.
- `engine/src/main/kotlin/com/roni/gymcraft/engine/content/Outpost.kt` — the Outpost race definition.
- `engine/src/main/kotlin/com/roni/gymcraft/engine/content/Brood.kt` — the Brood race definition.
- `engine/src/main/kotlin/com/roni/gymcraft/engine/content/DefaultContent.kt` — the race registry and default `GameContent`.

## :engine — economy
- `engine/src/main/kotlin/com/roni/gymcraft/engine/economy/Economy.kt` — new game (warm-up until Sunday), session (+100, comeback rally, heals, frees, ends leave), home card, train, upgrade, leave.

## :engine — battle
- `engine/src/main/kotlin/com/roni/gymcraft/engine/battle/BattleSim.kt` — the deterministic squad battle (lane, counters, no overkill, splash, healing) plus its `BattleRecord` for playback.

## :engine — waves
- `engine/src/main/kotlin/com/roni/gymcraft/engine/waves/Waves.kt` — the schedule (raids Sun–Fri, boss Sat 20:00, 12 weeks) and the waves.
- `engine/src/main/kotlin/com/roni/gymcraft/engine/waves/Campaign.kt` — fights every battle that fell due: wounds, occupation.
- `engine/src/main/kotlin/com/roni/gymcraft/engine/waves/Forecast.kt` — the next boss: wave, main trait, counter, win chance, sessions needed.
- `engine/src/main/kotlin/com/roni/gymcraft/engine/waves/Planner.kt` — how a sensible player spends (the forecast's "what a session buys", and the balance test's player).

## :engine — tests
- `engine/src/test/kotlin/com/roni/gymcraft/engine/economy/EconomyTest.kt` — economy rules.
- `engine/src/test/kotlin/com/roni/gymcraft/engine/battle/BattleSimTest.kt` — battle simulation rules.
- `engine/src/test/kotlin/com/roni/gymcraft/engine/waves/WavesTest.kt` — schedule and waves.
- `engine/src/test/kotlin/com/roni/gymcraft/engine/waves/CampaignTest.kt` — due battles, wounds and occupation.
- `engine/src/test/kotlin/com/roni/gymcraft/engine/waves/ForecastTest.kt` — the forecast.
- `engine/src/test/kotlin/com/roni/gymcraft/engine/waves/BalanceTest.kt` — the design's odds, plus missed weeks and comebacks.
- `engine/src/test/kotlin/com/roni/gymcraft/engine/testing/Seasons.kt` — helper that simulates seasons for the balance test.

## :app — build and shell
- `app/src/main/AndroidManifest.xml` — the single `.MainActivity`, `App`, dark theme, launcher icon, no permissions.
- `app/src/main/java/com/roni/gymcraft/App.kt` — `@HiltAndroidApp` Application.
- `app/src/main/java/com/roni/gymcraft/MainActivity.kt` — `@AndroidEntryPoint` activity; sets `GymCraftTheme { AppRoot() }`.
- `app/src/main/res/values/strings.xml` — `app_name`.
- `app/src/main/res/values/themes.xml` — `Theme.GymCraft`, dark window/status/navigation colors.
- `app/src/main/res/drawable/ic_launcher.xml` — placeholder vector: dark rounded square with an orange shield.

## :app — designsystem
- `app/src/main/java/com/roni/gymcraft/designsystem/Theme.kt` — dark Material 3 color scheme and `GymColors` for Canvas drawing.
- `app/src/main/java/com/roni/gymcraft/designsystem/sprites/CutoutSprites.kt` — builds the cutout part bitmaps (body, weapon, flash, stripe) and the badge layouts once; `rememberCutoutSprites()`.

## :app — navigation and screens
- `app/src/main/java/com/roni/gymcraft/navigation/AppRoot.kt` — Navigation 3 back stack: `HomeKey`, and `SpriteTestKey` in debug builds.
- `app/src/main/java/com/roni/gymcraft/feature/home/Home.kt` — Home state/action/effect, `HomeViewModel`, route and screen with the title and the "Sprite test" button.
- `app/src/main/java/com/roni/gymcraft/feature/spritetest/SpriteTest.kt` — sprite-test state/action, `SpriteTestViewModel`, route, screen and the `Battlefield` canvas.
- `app/src/main/java/com/roni/gymcraft/feature/spritetest/SpriteWorld.kt` — preallocated squad arrays; step (walk, bob, recoil, flash) and allocation-free draw.
- `app/src/main/java/com/roni/gymcraft/feature/spritetest/FpsMeter.kt` — counts frames and the worst frame gap per second.

## :app — tests
- `app/src/test/java/com/roni/gymcraft/feature/spritetest/FpsMeterTest.kt` — `oneSecondOf60Frames`.
