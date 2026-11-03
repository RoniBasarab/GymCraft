# GymCraft codemap (0.3.0)

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
- `engine/src/main/kotlin/com/roni/gymcraft/engine/content/Matchup.kt` — trait counters: the matchup against a boss's main trait.

## :engine — save
- `engine/src/main/kotlin/com/roni/gymcraft/engine/save/SaveCodec.kt` — the save's JSON field by field, version 1: a newer file is refused, garbage is "broken".
- `engine/src/test/kotlin/com/roni/gymcraft/engine/save/SaveCodecTest.kt` — round trip, version and broken-file handling.

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

## :app — core
- `app/src/main/java/com/roni/gymcraft/core/save/SaveStore.kt` — `files/save.json` written atomically (tmp → bak → move); reads the save or the backup, sets unreadable files aside.
- `app/src/main/java/com/roni/gymcraft/core/game/GameRepository.kt` — the one owner of the game: load or new, fight due battles, save, publish a `GameSnapshot`; one change at a time.
- `app/src/main/java/com/roni/gymcraft/core/game/GameClock.kt` — `PhoneClock` in release, `DebugClock` in debug (offset in `files/debug_clock.txt`, forward only).
- `app/src/main/java/com/roni/gymcraft/core/logging/AppLogger.kt` — the log file (`files/logs/gymcraft.log`).
- `app/src/main/java/com/roni/gymcraft/core/system/Failures.kt` — report/launch/reportFailures and `FailurePolicy` (loud in debug).
- `app/src/main/java/com/roni/gymcraft/core/system/Rescue.kt` — the Rescue interface behind the error card's buttons.
- `app/src/main/java/com/roni/gymcraft/core/system/AppRescue.kt` — the Android Rescue: restart the app, share the log through the FileProvider.
- `app/src/main/java/com/roni/gymcraft/core/di/AppModule.kt` — Hilt wiring: store, clock, logger, repository, rescue, policy.

## :app — designsystem
- `app/src/main/java/com/roni/gymcraft/designsystem/Theme.kt` — dark Material 3 color scheme and `GymColors` for Canvas drawing.
- `app/src/main/java/com/roni/gymcraft/designsystem/ErrorCard.kt` — "Something went wrong" with Restart app and Send log.
- `app/src/main/java/com/roni/gymcraft/designsystem/GymSheet.kt` — the bottom sheet used by building details.
- `app/src/main/java/com/roni/gymcraft/designsystem/HoldButton.kt` — the hold-to-confirm button ("I WENT").
- `app/src/main/java/com/roni/gymcraft/designsystem/sprites/CutoutSprites.kt` — builds the cutout part bitmaps (body, weapon, flash, stripe) and the badge layouts once; `rememberCutoutSprites()`.
- `app/src/main/java/com/roni/gymcraft/designsystem/sprites/SpriteArt.kt` — art ids to sprite parts for the game's races.
- `app/src/main/java/com/roni/gymcraft/designsystem/sprites/SpriteDrawing.kt` — draws a sprite from its parts with translate/rotate/scale.
- `app/src/main/java/com/roni/gymcraft/designsystem/sprites/content/OutpostArt.kt` — the Outpost sprites (generated from the canvas, render-checked).
- `app/src/main/java/com/roni/gymcraft/designsystem/sprites/content/BroodArt.kt` — the Brood sprites (generated from the canvas, render-checked).
- `app/src/main/java/com/roni/gymcraft/designsystem/sprites/content/SpriteIndex.kt` — the art registry: `ArtId` to sprite.
- `app/src/main/res/xml/file_paths.xml` — what the log FileProvider may share: only the logs folder.

## :app — navigation and screens
- `app/src/main/java/com/roni/gymcraft/navigation/AppRoot.kt` — Navigation 3 back stack: `HomeKey`, and `SpriteTestKey` in debug builds.
- `app/src/main/java/com/roni/gymcraft/feature/home/Home.kt` — Home state/action/effect and `HomeViewModel` (snapshot → state, every tap through `onAction`).
- `app/src/main/java/com/roni/gymcraft/feature/home/HomeScreen.kt` — Home's route and screen: top bar, leave banner, base, sergeant, forecast, "While you were away", home card, hold "I WENT".
- `app/src/main/java/com/roni/gymcraft/feature/home/HomeUi.kt` — small Home UI pieces and the menu.
- `app/src/main/java/com/roni/gymcraft/feature/home/HomeCards.kt` — the forecast card, the "While you were away" card and the home card.
- `app/src/main/java/com/roni/gymcraft/feature/home/SergeantLine.kt` — what the drill sergeant says after each tap and each battle.
- `app/src/main/java/com/roni/gymcraft/feature/home/BaseView.kt` — the base picture: HQ, buildings (tap → sheet), army with badges and wounded; idle animation at 30 fps.
- `app/src/main/java/com/roni/gymcraft/feature/spritetest/SpriteTest.kt` — sprite-test state/action, `SpriteTestViewModel`, route, screen and the `Battlefield` canvas.
- `app/src/main/java/com/roni/gymcraft/feature/spritetest/SpriteWorld.kt` — preallocated squad arrays; step (walk, bob, recoil, flash) and allocation-free draw.
- `app/src/main/java/com/roni/gymcraft/feature/spritetest/FpsMeter.kt` — counts frames and the worst frame gap per second.

## :app — tests
- `app/src/test/java/com/roni/gymcraft/feature/spritetest/FpsMeterTest.kt` — `oneSecondOf60Frames`.
- `app/src/test/java/com/roni/gymcraft/core/save/SaveStoreTest.kt` — write, read, backup fallback, unreadable files kept aside.
- `app/src/test/java/com/roni/gymcraft/core/game/GameRepositoryTest.kt` — load/new, due battles before a change, snapshot publishing.
- `app/src/test/java/com/roni/gymcraft/core/game/GameClockTest.kt` — the debug clock's forward-only offset.
- `app/src/test/java/com/roni/gymcraft/feature/home/SergeantLineTest.kt` — the sergeant's lines.
- `app/src/test/java/com/roni/gymcraft/feature/home/HomeUiTest.kt` — Home's state pieces (labels, costs, badges).
- `app/src/test/java/com/roni/gymcraft/feature/home/HomeViewModelTest.kt` — Home's `onAction` drives the repository and the state.
