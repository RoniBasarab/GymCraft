# TASK 2: The engine (0.2.0)

**Target version: 0.2.0** (versionCode 2). Branch `task-2/v0.2.0` from `main`. Everything is in `:engine`; the app doesn't change except its version.

The planning agent wrote, compiled and tested the whole engine. It ran thousands of simulated seasons to tune it, so that a steady 3 sessions a week wins the boss about 85% of the time, 2 about 55%, 1 about 5%. Your job is to bring it in, build it, document it and release it.

## Rules

1. `git checkout -b task-2/v0.2.0` ; `git add -A` ; `git rm docs/Task-1.md` ; `git commit -m "task-2 prep: docs"`.
   - This bundle's files are already in place (Roni unzipped it): the new `docs/`, and the engine sources and tests.
   - The prep commit takes them all. Then `task-2 slice N: <title>`.
2. **Shipped and verified, don't edit:** every file under `engine/src/` that this bundle contains (12 sources, 7 test files) and the existing `model/Ids.kt`.
3. Tests: only the shipped ones. All green.

---

## Slice 1: Build the engine

The bundle added these files. Don't touch them; just build.

| Package | File | What |
|---|---|---|
| model | `Defs.kt` | `Stats`, `UnitDef`, `BuildingDef`, `HqDef`, `RaceDef`, `GameContent`: races as data |
| model | `GameState.kt` | the whole game state, `BuildingState`, `LeavePeriod`, `BattleOutcome` |
| content | `Traits.kt`, `Outpost.kt`, `Brood.kt`, `DefaultContent.kt` | the only place race names appear |
| economy | `Economy.kt` | new game (warm-up until Sunday), session (+100, comeback rally, heals, frees, ends leave), home card, train, upgrade, leave |
| battle | `BattleSim.kt` | the deterministic squad battle (lane, counters, no overkill, splash, healing) plus its `BattleRecord` for playback |
| waves | `Waves.kt` | the schedule (raids Sun–Fri, boss Sat 20:00, 12 weeks) and the waves |
| waves | `Campaign.kt` | fights every battle that fell due: wounds, occupation |
| waves | `Forecast.kt` | the next boss: wave, main trait, counter, win chance, sessions needed |
| waves | `Planner.kt` | how a sensible player spends (the forecast's "what a session buys", and the balance test's player) |

Tests:
- `EconomyTest`, `BattleSimTest`, `WavesTest`, `CampaignTest`, `ForecastTest`;
- `BalanceTest` (the design's odds, plus missed weeks and comebacks);
- the helper `testing/Seasons.kt`.

Build: `:engine:test`. `BalanceTest` takes several seconds; that's expected.

If a shipped file doesn't compile, it's a real-name mismatch with the repo:
- fix only the mismatch (an import, a package);
- note it in `docs/NOTES.md`;
- never change logic or a test's expectations.

---

## Slice 2: Docs and release 0.2.0

1. **`docs/CODEMAP.md`:** one line per new engine file (the table above in short).
2. **`docs/NOTES.md`:** anything you had to adapt.
3. **`app/build.gradle.kts`:** `versionCode = 2`, `versionName = "0.2.0"`.
4. **`CHANGELOG.md`:**
   ```markdown
   ## 0.2.0
   - The engine: races as data (the Outpost, the Brood), the economy, the weekly schedule and waves, the squad battle with counters, the forecast. Balanced by simulation: 3 sessions a week wins the boss ~85%, 2 ~55%, 1 ~5%.
   ```
5. **Final gate:** `:engine:test :app:testDebugUnitTest :app:assembleDebug`. Then:
   ```
   git checkout main ; git merge --no-ff task-2/v0.2.0 -m "merge task-2/v0.2.0" ; git tag v0.2.0 ; git push origin main v0.2.0
   ```
   A report of at most 6 lines, then stop.
