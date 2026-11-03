# GymCraft: planning handoff

Last updated: 3 Nov 2026 (after Task-1; Task-2 written). For the planning agent (a Claude chat with Roni). The working agent doesn't read this file.

## People and flow

- **Roni** decides everything. He's a senior Android/Kotlin developer, and this is his own app (Play Store maybe later).
- **The planning agent** (Claude) designs with him through questionnaires, writes `docs/Task-N.md`, `docs/AGENT.md` and verified Kotlin for the tricky rules, sends zip bundles, reviews the working agent's commits, and writes nudge prompts.
- **The working agent** (OpenCode on Windows) implements one task per session.
  - Start prompt: `Read "C:/GymCraft/docs/AGENT.md" and do what it says.`
  - The same line resumes after a quota stop.
- **Same way of working as Blocks:**
  - ask with options and pros/cons;
  - never deviate from the architecture alone;
  - verified pure code for the tricky logic;
  - small versions;
  - a short phone checklist per version.

## Decisions so far

- **Game:** `docs/game-design.md` (all of Roni's answers, 3 Nov).
- **Architecture:** `docs/architecture.md`:
  - `:app` + pure `:engine`;
  - races as data;
  - one JSON save file;
  - minSdk 29;
  - Hilt, Nav3, Compose Canvas;
  - simulate → record → play back.
- **Agent rules:** `docs/AGENT.md`, every lesson from Blocks.
- **Art:** the first sprite designs are on the "GymCraft Sprites" canvas (Outpost units, Outpost buildings, Brood attackers). They're original vector cutouts in pieces (body, weapon, flash) with a team-color stripe. Roni reviews them; the plan is AI-generated sprites, cleaned up, in that style.
- **Gym visits:** a manual button. Geofencing was dropped (no background location, Play policy).
- **First race:** the Outpost; the Brood is the season-1 enemy; the Radiant comes later.
- **Squads with count badges** on the battlefield (Roni's idea).

## Roadmap

1. **Task-1 = 0.1.0:** done 3 Nov (tag v0.1.0). The project from nothing, plus the sprite-test spike; waiting for Roni's fps numbers (debug build).
2. **Task-2 = 0.2.0** (written 3 Nov): the engine, verified (32 JVM tests, compiled and run by the planning agent).
   - **Battle model:** squads in a lane, no overkill (one shot drops at most its splash count), counters (Rocketeer ×2 vs armored; Tank ×2 + splash 4 vs swarm), healing.
   - **Calendar:** warm-up until the first Sunday.
   - **Tuned:** boss 240 + 265/week (×1.3 in week 12), raids 30%. Measured boss wins: 3/wk ~85%, 2/wk ~55%, 1/wk ~5%, 0 = 0%. One week off after a streak → ~45%, two → ~10%, two weeks after returning ~65%.
   - **Overkill fix:** before it, Rocketeers beat everything.
3. **Task-3:** save file, Home (base, supplies, "I went", forecast), training and upgrades.
4. **Task-4:** the battle player plus Rewatch; scheduled battles; notifications; leave.
5. **Later:** seasons and perks; real art; more races.

## Roni's preferences that matter here

- Architecture is binding: State / sealed Action / Effect / one `onAction`; no callback-parameter lists; ask before deviating.
- All tests green; never change test expectations.
- No `!!`, silent failures with an error card.
- Fast and decisive working agent.
- He dislikes hassle. Short run sheets with exact commands.
- Don't bring up signing unless it matters.
- In text he sends as himself: no em dashes, no AI-sounding phrasing.
