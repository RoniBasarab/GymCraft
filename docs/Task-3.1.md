# TASK 3.1: The base is the screen (0.3.1)

**Target version: 0.3.1** (versionCode 4). Branch `task-3.1/v0.3.1` from `main`. Roni's feedback on 0.3.0 (game-design.md §10, §12):
- Home becomes the base, full screen.
- The boss and the home card shrink to one line each.
- Buildings are labelled "Barracks 1".
- The currency is **protein**, with a tub icon on every price.
- More life on the base: doors, radar, flickering windows, a training yard, the drill recruit on the home card, a lift for "I went", and the troops cheer.

No engine change. The save format doesn't change: the code still calls the currency `supplies`; only the screen says protein.

App sources: `app/src/main/java/com/roni/gymcraft/...`.

## Rules

1. **Prep:** `git checkout -b task-3.1/v0.3.1` ; `git rm app/src/main/java/com/roni/gymcraft/designsystem/HoldButton.kt docs/Task-3.md` ; `git add -A` ; `git commit -m "task-3.1 prep: docs"`.
   - Roni unzipped this bundle over the repo, so its files are already in place.
   - `HoldButton.kt` is replaced by `LiftButton.kt`.
2. **Verified (compiled and tested by the planner), don't edit:**
   - `feature/home/`: `Home.kt` (adds `HomeState.cheers`), `HomeUi.kt` (`GameUi.protein`, `HomeCardUi.move/hold`, the texts), `HomeCards.kt` (each card has a move), `SergeantLine.kt` (protein);
   - `designsystem/sprites/content/`: `OutpostArt.kt`, `BroodArt.kt`, `UiArt.kt` (new), `SpriteIndex.kt`. The art is regenerated and render-checked; the new pieces are on the canvas's "0.3.1 · Gym life" board;
   - tests: `feature/home/SergeantLineTest`, `HomeUiTest`, `HomeViewModelTest`.
3. **Test expectation changes in this task, on purpose (Roni's decisions):**
   - "supplies" → "protein" in the sergeant's lines and the refusal;
   - "Barracks: level 2." → "Barracks 2.";
   - the home card now carries its move.
   
   They come with the bundle; don't change any other expectation.
4. **Planner-written, not compiled** (AGENT.md: fix only what the compiler names):
   - `designsystem/LiftButton.kt` (new), `designsystem/sprites/SpriteDrawing.kt`;
   - `feature/home/BaseView.kt`, `feature/home/HomeScreen.kt`.
5. **`docs/art/`** (new): the sprites' SVG sources. Documentation only; nothing builds from them.

---

## Slice 1: Build it

1. `:app:compileDebugKotlin`. Fix only what the errors name, in the rule-4 files. Likely spots:
   - `OutlinedButton` / `Button` `contentPadding`;
   - `rememberSaveable` + `mutableIntStateOf` imports in `BaseView`;
   - `DrawScope.scale(scaleX, scaleY, pivot)` / `rotate(degrees, pivot)` parameter names in `SpriteDrawing`.
2. `:app:testDebugUnitTest`: all green (the three updated test classes included).
3. `codeRules` stays green: no race names outside `content/`. The new art ids are built by `SpriteIndex` (`yard`, `drill`, `portrait`, `PROTEIN`), never spelled out in feature code.

Then the full gate.

---

## Slice 2: Docs and release 0.3.1

1. **`docs/CODEMAP.md`:** add `LiftButton.kt`, `UiArt.kt`, `docs/art/`; remove `HoldButton.kt`.
2. **`docs/NOTES.md`:** each fix from slice 1, one line each.
3. **`app/build.gradle.kts`:** `versionCode = 4`, `versionName = "0.3.1"`.
4. **`CHANGELOG.md`:**
   ```markdown
   ## 0.3.1
   - Home is the base, full screen: buildings on the back row ("Barracks 1"), the HQ and a new training yard in front, the army at the bottom.
   - The currency is protein (a protein tub on the top bar and on every price).
   - The boss forecast and the home card are one line each; a little recruit does the day's exercise.
   - "I went" is a lift: hold it and a barbell rises; at the top the troops cheer.
   - More life on the base: the Barracks door opens and a recruit peeks out, the garage door rolls up, the radar turns, windows flicker, the flag gusts, pull-ups and boxing in the yard.
   ```
5. **`docs/MANUAL_TESTS.md`:** replace with "Check on the phone (0.3.1)", debug build:
   1. Home fills the screen. The sky strip shows the tub with the protein and the ⋮ menu. The scene shows "Barracks 1" and "Motor Pool 1" (no text running together), the HQ, the training yard and the army.
   2. Watch for half a minute:
      - the Barracks door opens and a recruit peeks out (none visible while it's closed);
      - the garage door rolls up;
      - the radar turns and a window flickers;
      - a recruit does pull-ups and another punches the bag.
   3. The boss and the home card are one line each. The recruit next to the card does its move.
   4. Hold **I WENT** and let go halfway: the barbell drops back and nothing happens. Hold it to the top: it locks out with a buzz, +100 protein, and the troops hop with a raised fist.
   5. Tap the Barracks: prices show the tub ("Train 150"). Train and upgrade: "Upgrade to Barracks 2", then the label on the base reads "Barracks 2".
6. **If `adb devices` lists a phone:** install the debug build only (`com.roni.gymcraft.debug`). Never touch any Blocks app.
7. **Final gate:** `:engine:test :app:testDebugUnitTest :app:assembleDebug`. Then:
   ```
   git checkout main ; git merge --no-ff task-3.1/v0.3.1 -m "merge task-3.1/v0.3.1" ; git tag v0.3.1 ; git push origin main v0.3.1
   ```
   A report of at most 6 lines (the slice 1 fixes in one line), then stop.
