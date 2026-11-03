## 0.3.0
- The game is saved (one JSON file, written atomically, with a backup; a broken save is kept aside, never deleted).
- Home: the base with your buildings and army, the drill sergeant, the next boss's forecast, "While you were away", the daily home card, and hold "I WENT" (+100).
- Buildings: train units (pick how many) and upgrade; an occupied building shows it.
- Going on leave and I'm back, in the menu.
- Battles are fought when they fall due (on opening the app). Watching them comes in 0.4.0.
- Failures are logged and shown as an error card with Restart app and Send log.
- Debug: time travel in the menu (+1 hour, +1 day, back to real time).

## 0.2.0
- The engine: races as data (the Outpost, the Brood), the economy, the weekly schedule and waves, the squad battle with counters, the forecast. Balanced by simulation: 3 sessions a week wins the boss ~85%, 2 ~55%, 1 ~5%.

## 0.1.0
- The project: :engine (game rules, pure Kotlin) and :app (Compose).
- Developer tool: Sprite test, to measure how many cutout sprites the phone draws at 60 fps.
