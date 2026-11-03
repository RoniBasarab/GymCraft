# TASK 1: The project and a rendering test (0.1.0)

**Target version: 0.1.0** (versionCode 1). The first task of GymCraft: create the project from nothing, build it, and prove that Compose can draw the battlefield fast enough.

- **Folder:** `C:/GymCraft`.
- **Branch:** `task-1/v0.1.0`, merged into `main` at the end.
- **GitHub:** https://github.com/RoniBasarab/GymCraft (new, maybe empty).

Paths start at the repo root.

## What's already there

Roni created `C:/GymCraft` and unzipped this bundle into it. It contains:
- `docs/` (this task, AGENT.md, architecture.md, game-design.md, SUMMARY.md);
- `gradle/codeRules.gradle.kts` and `gradle/libs.versions.toml`;
- `settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`, `compose_stability.conf`, `.gitignore`;
- `engine/build.gradle.kts` and `app/build.gradle.kts`.

**Use every one of these files as it is** (shipped). There is no git, no Gradle wrapper and no source code yet.

## Rules

1. **Architecture** (`docs/architecture.md`) is binding from the first file.
2. **Tests:** JVM tests only. Add only the tests named here.
3. **Commits:**
   - `task-1 prep: docs` (slice 0), then `task-1 slice N: <title>`;
   - tag `v0.1.0` on `main` at the end.
4. **Never touch `C:/Blocks App`** except to *copy* the four wrapper files in slice 0. Don't edit, build or commit anything there.

---

## Slice 0: Git and the wrapper

From `C:/GymCraft`:
```
git init -b main ; git remote add origin https://github.com/RoniBasarab/GymCraft.git ; git ls-remote origin
```
- If `ls-remote` lists `refs/heads/main` (GitHub created a README or licence): `git pull origin main`. Otherwise go on.
- Copy the Gradle wrapper from Blocks (same Gradle 9.6.0, already downloaded on this PC):
  ```
  Copy-Item "C:/Blocks App/gradlew","C:/Blocks App/gradlew.bat" . ; Copy-Item "C:/Blocks App/gradle/wrapper" "gradle/" -Recurse
  ```
- Then:
  ```
  git add -A ; git commit -m "task-1 prep: docs" ; git push -u origin main ; git checkout -b task-1/v0.1.0
  ```

---

## Slice 1: The skeleton builds

Create only these sources (package `com.roni.gymcraft`; the engine's is `com.roni.gymcraft.engine`):

**`:engine`**
- `engine/src/main/kotlin/com/roni/gymcraft/engine/model/Ids.kt`:
  ```kotlin
  package com.roni.gymcraft.engine.model

  import kotlinx.serialization.Serializable

  /** Every game object is named by an id (architecture.md §4): races are data, never code. */
  @Serializable @JvmInline value class RaceId(val value: String)
  @Serializable @JvmInline value class BuildingId(val value: String)
  @Serializable @JvmInline value class UnitId(val value: String)
  @Serializable @JvmInline value class TraitId(val value: String)
  @Serializable @JvmInline value class ArtId(val value: String)
  ```
- `engine/src/test/kotlin/com/roni/gymcraft/engine/model/IdsTest.kt`: `idsCompareByValue` (`UnitId("a") == UnitId("a")`, `!= UnitId("b")`).

**`:app`**
- `app/src/main/AndroidManifest.xml`:
  - `android:name=".App"`, label `@string/app_name`, icon `@drawable/ic_launcher`, theme `@style/Theme.GymCraft`;
  - one activity, `.MainActivity`, exported, with the MAIN/LAUNCHER filter;
  - no permissions.
- **Resources:**
  - `res/values/strings.xml`: `app_name` = `GymCraft`;
  - `res/values/themes.xml`: `Theme.GymCraft`, parent `android:Theme.Material.NoActionBar`, `android:windowBackground` `#1C1915`, `android:statusBarColor` and `android:navigationBarColor` `#1C1915`;
  - `res/drawable/ic_launcher.xml`: a simple vector, a dark rounded square (`#1C1915`) with an orange (`#E07B28`) shield shape. A placeholder until real art.
- **`App.kt`:** `@HiltAndroidApp class App : Application()`.
- **`MainActivity.kt`:** `@AndroidEntryPoint`; `setContent { GymCraftTheme { AppRoot() } }`.
- **`designsystem/Theme.kt`:** `GymCraftTheme`, a dark Material 3 color scheme:
  - background `#1C1915`, surface `#28231D`, onBackground / onSurface `#F1E9DA`, primary `#E07B28`, onPrimary `#1C1915`;
  - the canvas palette's muted text `#BCAE96` as `onSurfaceVariant`.
  
  Plus `object GymColors` with those values for Canvas drawing.
- **`navigation/AppRoot.kt`:**
  - a Navigation 3 back stack with two keys, `HomeKey` and (debug builds only) `SpriteTestKey`;
  - `HomeKey` → `HomeRoute`; `SpriteTestKey` → `SpriteTestRoute` (slice 2; in this slice a placeholder `Text`).
- **`feature/home/Home.kt`**, the MVI shape from architecture.md §6 even though it's tiny:
  - `@Immutable data class HomeState(val title: String = "GymCraft", val showSpriteTest: Boolean = BuildConfig.DEBUG)`;
  - `sealed interface HomeAction { data object OpenSpriteTest : HomeAction }`;
  - `sealed interface HomeEffect { data object OpenSpriteTest : HomeEffect }`;
  - `@HiltViewModel class HomeViewModel @Inject constructor() : ViewModel()` with `onAction`;
  - `HomeRoute` collects the state and the effects (navigation);
  - `HomeScreen(state, onAction)` shows the title (large) and, when `showSpriteTest`, a button "Sprite test".

**First build: online, once.** The `kotlin-jvm` plugin may not be in the cache yet. Drop `--offline` for this one build only:
```
cmd /c "gradlew.bat :engine:test :app:assembleDebug --console=plain > %TEMP%\g.txt 2>&1" ; "exit=$LASTEXITCODE" ; Select-String -CaseSensitive -Path $env:TEMP\g.txt -Pattern '^e: |FAILED|What went wrong' -Context 0,3 | Select-Object -First 25
```
From then on, always `--offline`.

**Test:** `IdsTest.idsCompareByValue`.

---

## Slice 2: Sprite test (debug only)

**Goal:** a measured answer to "can Compose draw a battlefield of cutout sprites at 60 fps on Roni's phone?". This is a spike: its screen stays as a developer tool, nothing in the game depends on it.

**`designsystem/sprites/CutoutSprites.kt`:**
- Builds the **parts once** as `ImageBitmap`s, by drawing simple shapes into them with `androidx.compose.ui.graphics.Canvas(ImageBitmap(...))`:
  - **body:** a rounded body, a round helmet and a light-blue visor, with dark outlines;
  - **weapon:** a grey bar;
  - **flash:** a yellow star;
  - **team stripe:** orange.
- Each part is about 64×64 px at 1x, scaled by density.
- `rememberCutoutSprites(): CutoutSprites`.

**`feature/spritetest/SpriteTest.kt`:**
- **State and actions:**
  - `@Immutable data class SpriteTestState(val count: Int = 200, val fps: String = "")`;
  - `sealed interface SpriteTestAction { data class SetCount(val count: Int) : SpriteTestAction ; data class Measured(val fps: Int, val worstMs: Int) : SpriteTestAction }`;
  - `SpriteTestViewModel` keeps them; `fps` text is `"58 fps · worst frame 21 ms"`.
- **Screen:** `SpriteTestScreen(state, onAction)`: a full-screen battlefield `Canvas`, with on top three buttons `50 · 200 · 500` and the fps text.
- **The battlefield**, exactly this pattern (architecture.md §5, §11):
  ```kotlin
  @Composable
  fun Battlefield(count: Int, onAction: (SpriteTestAction) -> Unit, modifier: Modifier = Modifier) {
      val sprites = rememberCutoutSprites()                 // part bitmaps, built once
      val world = remember(count) { SpriteWorld(count) }    // FloatArrays, built once per count
      var frameNanos by remember { mutableLongStateOf(0L) }
      val action by rememberUpdatedState(onAction)

      LaunchedEffect(world) {
          val meter = FpsMeter()
          while (true) withFrameNanos { now ->
              frameNanos = now
              meter.frame(now)?.let { (fps, worst) -> action(SpriteTestAction.Measured(fps, worst)) }   // once a second
          }
      }
      Canvas(modifier.fillMaxSize()) {
          val t = frameNanos             // read only here: a new frame redraws, never recomposes
          world.step(t, size.width, size.height)
          world.draw(this, sprites, t)
      }
  }
  ```
  It takes the screen's `onAction`, like every composable (architecture.md §6).
- **`SpriteWorld(count)`:**
  - **Data:** preallocated `FloatArray`s for x, y, speed and phase.
  - **Step:** each squad walks right-to-left and wraps around, bobs (`sin`), and every 1.6 s (offset by index) recoils its weapon by a few px and shows the flash for 80 ms.
  - **Draw:** each squad as body + stripe + weapon + flash with `drawImage` under `translate`/`scale`, plus a count badge ("×5"). The badge text comes from `TextLayoutResult`s measured **once** in `remember` for the few numbers used.
  - **No allocation in `step` or `draw`:** no lists, no `Path`, no lambdas, no `Offset` objects in loops. Use primitive math and `withTransform` / `translate`.
- **`FpsMeter`:** counts frames and the worst frame gap over each second. Returns a pair once a second, else null. Reuse one result holder to avoid allocating every second; a `Pair` once a second is also acceptable.
- **Navigation:** Home's "Sprite test" button → `SpriteTestKey`.

**Test:** `FpsMeterTest.oneSecondOf60Frames`, a plain JVM test in `:app`'s `test` (no Robolectric): feed 61 timestamps 16.67 ms apart → (60, 17).

---

## Slice 3: Docs and release 0.1.0

1. **`docs/CODEMAP.md`** (new): one line per source file, what it holds.
2. **`docs/NOTES.md`** (new): one line per choice you made.
3. **`docs/MANUAL_TESTS.md`** (new), "Check on the phone (0.1.0)":
   1. GymCraft Debug opens on the dark Home screen with the title.
   2. **Sprite test** at 50, 200 and 500 squads: note the fps and the worst frame for each.
4. **`CHANGELOG.md`** (new):
   ```markdown
   ## 0.1.0
   - The project: :engine (game rules, pure Kotlin) and :app (Compose).
   - Developer tool: Sprite test, to measure how many cutout sprites the phone draws at 60 fps.
   ```
5. **If `adb devices` lists a phone:** install the debug build only (`app\build\outputs\apk\debug\app-debug.apk`, package `com.roni.gymcraft.debug`) and say so in the report. Never touch any Blocks app.
6. **Final gate:** `:engine:test :app:testDebugUnitTest :app:assembleDebug`. Then:
   ```
   git checkout main ; git merge --no-ff task-1/v0.1.0 -m "merge task-1/v0.1.0" ; git tag v0.1.0 ; git push origin main v0.1.0
   ```
   A report of at most 8 lines, then stop.
