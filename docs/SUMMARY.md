# GymCraft: planning handoff

Last updated: 3 Nov 2026, late (Task-3 done by Roni on the phone; Task-3.1 written). For the planning agent (a Claude chat with Roni). The working agent doesn't read this file.

## People and flow

- **Roni** decides everything. He's a senior Android/Kotlin developer, and this is his own app (Play Store maybe later).
- **The planning agent** (Claude) designs with him through questionnaires, writes `docs/Task-N.md`, `docs/AGENT.md` and verified Kotlin for the tricky rules, sends zip bundles, reviews the working agent's commits, and writes nudge prompts.
- **The working agent** (OpenCode on Windows) implements one task per session.
  - Start prompt: `Read "C:/GymCraft/docs/AGENT.md" and do what it says.`
  - The same line resumes after a quota stop.
- **Same way of working as Blocks:** ask with options and pros/cons; never deviate from the architecture alone; verified pure code for the tricky logic; small versions; a short phone checklist per version.

## Decisions so far

- **Game:** `docs/game-design.md` (§12 Home added with Task 3).
- **Architecture:** `docs/architecture.md`: `:app` + pure `:engine`; races as data; one JSON save file (manual codec, atomic, backup); minSdk 29; Hilt, Nav3, Compose Canvas; simulate → store the input → rerun for playback.
- **Agent rules:** `docs/AGENT.md`, every lesson from Blocks, plus "planner-written, not compiled" code: fix only compiler errors.
- **Art:** the "GymCraft Sprites" canvas (Outpost units and buildings, Brood attackers, the sergeant). The planner's generator turns the canvas SVGs into Kotlin `ImageVector` layers per moving part (`OutpostArt.kt`, `BroodArt.kt`; render-checked, at most 0.16% of pixels off; the tread dash is drawn as a plain stroke).
- **Gym visits:** a manual hold-to-confirm button. No geofencing.
- **Squads with count badges** (Roni's idea): one badge above each squad, wounded below.
- **0.3.0 feedback (3 Nov):** the game must fill the screen; cards too big; "Lv 1" labels ran together (now "Barracks 1"); not gym-like enough. Roni chose: full screen with overlays, one-line boss and home card, door and HQ animations, all four gym ideas (training yard, animated home card, the lift, gym gear), the currency renamed **protein**.
- **Art sources:** `docs/art/*.svg` in the repo; the generator is the planner's (`artgen/gen.py`: classes become parts, a moving part's layers share one union box for pivots). Canvas boards: Units, Buildings, Brood, "0.3.1 · Gym life", "An attack · Task 4 idea".

## Roadmap

1. **Task-1 = 0.1.0:** done 3 Nov. The project plus the sprite-test spike: 120 fps on Roni's phone at 120 Hz; worst frame 8 to 17 ms. The off-center muzzle flash and the overlapping badges were spike art; the real art has muzzle anchors.
2. **Task-2 = 0.2.0** (written 3 Nov): the engine, verified (32 JVM tests). Balanced: 3/wk ~85%, 2/wk ~55%, 1/wk ~5%.
3. **Task-3 = 0.3.0** (written 3 Nov): save, Home, buildings, leave, failures, debug time travel.
   - **Verified in the planner's harness (67 tests across engine and app):** SaveCodec, SaveStore, GameRepository, GameClock, SergeantLine, HomeUi, HomeCards, HomeViewModel (driven through onAction on a real repository).
   - **Planner-written, not compiled:** the Compose UI (BaseView, HomeScreen, HoldButton, GymSheet, ErrorCard, SpriteDrawing), AppModule, AppRescue.
4. **Task-3.1 = 0.3.1** (written 3 Nov): the base is the screen, protein, the lift, the yard, the drill recruit, more idle life. Verified: Home logic and art (67 harness tests); planner-written: LiftButton, SpriteDrawing, BaseView, HomeScreen.
5. **Task-4:** the battle player plus Watch / Rewatch (muzzle anchors from the art, lane spacing, badges), battles resolved on schedule while the app is open, notifications (Wednesday 18:00 forecast, results).
   The battle screen follows the "An attack" board (Roni asked how an attack would look; the mock is waiting for his OK): one lane, the HQ at the left edge, squads with count and health bar, the Brood from the right, shots from muzzles, both armies' strength on top, Pause / ×1 / ×2 / Skip.
6. **Upgrade cards** (decided 4 Nov, game-design.md §13): a pick-1-of-3 draft after every boss battle; no plain stat bumps; ~80-100 mechanics with ranks and per-race versions (400+ offers); engine hooks; balance test with random drafts. Open: does a comeback improve the draw (a 4th card or a rarer tier)? Planner's default: yes, a 4th card.
7. **Later:** seasons and perks; real art; more races. Possible optimization: crop each sprite layer's ImageVector to its bounds (less bitmap memory) before the battle player draws many sprites.

## Roni's preferences that matter here

- Architecture is binding: State / sealed Action / Effect / one `onAction`; no callback-parameter lists; ask before deviating.
- All tests green; never change test expectations.
- No `!!`, silent failures with an error card.
- Fast and decisive working agent.
- He dislikes hassle. Short run sheets with exact commands.
- Don't bring up signing unless it matters.
- In text he sends as himself: no em dashes, no AI-sounding phrasing.
