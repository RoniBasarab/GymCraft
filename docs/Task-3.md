# TASK 3: Save, Home and the base (0.3.0)

**Target version: 0.3.0** (versionCode 3). Branch `task-3/v0.3.0` from `main`. After this task GymCraft is playable day to day: the game is saved, Home shows the base, "I went" pays, buildings train and upgrade, battles are fought when they fall due, and the sergeant talks. The battle player (Watch, Rewatch) and notifications are Task 4.

Paths start at the repo root. App sources: `app/src/main/java/com/roni/gymcraft/...` (if Task 1 used `src/main/kotlin`, the bundle's files still compile from `java/`; leave them there).

## Rules

1. **Prep:** `git checkout -b task-3/v0.3.0` ; `git add -A` ; `git rm docs/Task-2.md` ; `git commit -m "task-3 prep: docs"`.
   - Roni unzipped this bundle over the repo, so its files are already in place, including a new `feature/home/Home.kt` that replaces Task 1's.
   - The prep commit takes them all, even though `:app` doesn't build yet. Then `task-3 slice N: <title>`.
2. **Verified (compiled and tested by the planner), don't edit:**
   - engine: `save/SaveCodec.kt`, `content/Matchup.kt`, test `save/SaveCodecTest.kt`;
   - app `core/`: `save/SaveStore.kt`, `game/GameRepository.kt`, `game/GameClock.kt`, `logging/AppLogger.kt`, `system/Failures.kt`, `system/Rescue.kt`;
   - app `feature/home/`: `Home.kt` (State, Action, Effect, ViewModel), `HomeUi.kt`, `HomeCards.kt`, `SergeantLine.kt`;
   - app `designsystem/sprites/`: `SpriteArt.kt`, `content/OutpostArt.kt`, `content/BroodArt.kt` (generated from the canvas, render-checked), `content/SpriteIndex.kt`;
   - app tests: `core/save/SaveStoreTest`, `core/game/GameRepositoryTest`, `core/game/GameClockTest`, `feature/home/SergeantLineTest`, `feature/home/HomeUiTest`, `feature/home/HomeViewModelTest`.
3. **Planner-written, not compiled** (AGENT.md: fix only what the compiler names):
   - `core/system/AppRescue.kt`, `core/di/AppModule.kt`, `res/xml/file_paths.xml`;
   - `designsystem/ErrorCard.kt`, `GymSheet.kt`, `HoldButton.kt`, `sprites/SpriteDrawing.kt`;
   - `feature/home/BaseView.kt`, `feature/home/HomeScreen.kt`.
4. **Tests:** only the shipped ones. All green, with Task 1 and Task 2's.

### What's new, in one screen

| Piece | What |
|---|---|
| `SaveCodec` (engine) | the save's JSON, field by field, version 1; a newer file is refused, garbage is "broken", never a crash |
| `SaveStore` | `files/save.json`: write tmp → copy the old save to `.bak` → atomic move; read the save, else the backup; unreadable files are renamed aside, never deleted |
| `GameRepository` | the one owner of the game: loads (or starts a new game), fights every battle that fell due before each change, saves, then publishes a `GameSnapshot` (state, seen battles, forecast). One change at a time (a `Mutex`); disk and simulation on `Dispatchers.IO` |
| `GameClock` / `TimeTravel` | `PhoneClock` in release; `DebugClock` in debug (the offset is kept in `files/debug_clock.txt`, forward only) |
| `Failures`, `AppLogger`, `Rescue`/`AppRescue`, `ErrorCard` | from Blocks 3.5.0: release logs and shows the error card, debug crashes; the card's Restart app / Send log |
| `HomeViewModel` + `HomeUi` | Home's state from the snapshot; every tap through `onAction`; the sergeant answers each tap (his bubble) |
| `BaseView` | the base picture: HQ, buildings (tap → sheet), army with one badge above each squad and its wounded below; idle animation at 30 fps |
| `HomeScreen` | top bar (supplies, ⋮ menu), leave banner, base, sergeant, forecast card, "While you were away", home card, hold "I WENT" |

---

## Slice 1: The engine's save format

Nothing to write: `SaveCodec.kt`, `Matchup.kt` and `SaveCodecTest.kt` are in place.

Build: `:engine:test`.

---

## Slice 2: The app compiles and its tests pass

All app files are in place. Make `:app` compile, in this order:

1. **Old Home:** `git grep -n "HomeState\|HomeRoute\|showSpriteTest" -- "*.kt"`.
   - If Task 1's Home code lives in a second file (another root or name), delete that old copy: `Home.kt` and `HomeScreen.kt` from the bundle replace it.
   - The "Sprite test" button is now in Home's ⋮ menu (debug only).
2. **`navigation/AppRoot.kt`:** the `HomeKey` entry calls `HomeRoute(openSpriteTest = { <what Task 1's Home did to open SpriteTestKey> })`. Change nothing else in AppRoot.
3. **`HomeRoute` imports:** if `hiltViewModel` doesn't resolve, use the import Task 1's `SpriteTestRoute` uses.
4. **`AndroidManifest.xml`**, inside `<application>`:
   ```xml
   <provider
       android:name="androidx.core.content.FileProvider"
       android:authorities="${applicationId}.logs"
       android:exported="false"
       android:grantUriPermissions="true">
       <meta-data
           android:name="android.support.FILE_PROVIDER_PATHS"
           android:resource="@xml/file_paths" />
   </provider>
   ```
5. **Build `:app:compileDebugKotlin`.** Fix only what the errors name, in the planner-written files (rule 3). Likely spots:
   - Material 3 parameter names in `ErrorCard`, `GymSheet` (`ModalBottomSheet`'s `sheetState` / `containerColor`), `HoldButton`;
   - `drawText` / `rememberTextMeasurer` imports in `BaseView`.
   
   `codeRules` must stay green: `@OptIn` only in `designsystem/`, no race names outside a `content/` folder.
6. **Build `:app:testDebugUnitTest`.** The six new test classes must pass. A failure means the code is wrong, not the test.
   - In a verified file, the cause is a real-name mismatch (an import, a package): fix only that and note it.
   - `HomeViewModelTest` sets the Main dispatcher itself (`Dispatchers.setMain`); it needs no Robolectric.

Then the full gate.

---

## Slice 3: Docs and release 0.3.0

1. **`docs/CODEMAP.md`:** one line per new file (the table above in short).
2. **`docs/NOTES.md`:** each fix from slice 2, one line each.
3. **`app/build.gradle.kts`:** `versionCode = 3`, `versionName = "0.3.0"`.
4. **`CHANGELOG.md`:**
   ```markdown
   ## 0.3.0
   - The game is saved (one JSON file, written atomically, with a backup; a broken save is kept aside, never deleted).
   - Home: the base with your buildings and army, the drill sergeant, the next boss's forecast, "While you were away", the daily home card, and hold "I WENT" (+100).
   - Buildings: train units (pick how many) and upgrade; an occupied building shows it.
   - Going on leave and I'm back, in the menu.
   - Battles are fought when they fall due (on opening the app). Watching them comes in 0.4.0.
   - Failures are logged and shown as an error card with Restart app and Send log.
   - Debug: time travel in the menu (+1 hour, +1 day, back to real time).
   ```
5. **`docs/MANUAL_TESTS.md`:** replace the content with "Check on the phone (0.3.0)", debug build (`com.roni.gymcraft.debug`):
   1. Home opens: **100** supplies; the HQ, Barracks Lv 1, Motor Pool Lv 1; "No troops yet"; the sergeant's line; the boss forecast card.
   2. Hold **I WENT**: the bar fills, the phone buzzes, **200** supplies, the bubble says "Good work, recruit. +100 supplies.", the button now says "Logged today". Tap the bubble: back to his forecast line.
   3. Home card **Done +10**: **210**, the button greys out.
   4. Tap the **Barracks**: Rifleman available, Medic and Rocketeer greyed ("Needs level 2", "Needs level 3"). Pick 3 with +, **Train · 150**: "3 Riflemen ready.", and a Rifleman with ×3 on the base.
   5. Menu → **Going on leave**: the banner, no forecast. **I'm back**: the banner goes.
   6. Menu → **Debug: time +1 day** (and +1 hour) until the game's time, top right, is past 20:00 on or after the first Sunday (a game started mid-week warms up until Sunday): "While you were away" lists the raids; **Got it** clears it.
   7. Close the app from recents and open it again: the same supplies, army and buildings.
6. **If `adb devices` lists a phone:** install the debug build only (`com.roni.gymcraft.debug`) and say so in the report. Never touch any Blocks app.
7. **Final gate:** `:engine:test :app:testDebugUnitTest :app:assembleDebug`. Then:
   ```
   git checkout main ; git merge --no-ff task-3/v0.3.0 -m "merge task-3/v0.3.0" ; git tag v0.3.0 ; git push origin main v0.3.0
   ```
   A report of at most 8 lines (include the fixes from slice 2 in one line), then stop.
