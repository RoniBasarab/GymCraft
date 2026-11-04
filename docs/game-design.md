# GymCraft: game design

Decided by Roni with the planning agent, 3 Nov 2026. **Binding**: a task may only build what is written here. Anything new goes back to Roni first, with pros and cons.

## 1. What it is for

GymCraft keeps Roni going to the gym, and above all brings him **back** after a missed week or two (his own or his kid's illness, emergencies). Most streak apps punish exactly then; GymCraft rewards returning.

A small base-builder in a chunky cartoon style inspired by StarCraft: Brood War. All races, units and names are **original**: inspired by, never copied (it may go on the Play Store later).

## 2. The loop

1. **Go to the gym → hold "I went"** (manual; no location, no permission). +100 protein.
2. **Spend protein:** train units, upgrade production buildings.
3. **Attacks come on a schedule.** The battle runs by itself; you watch it as a short animation (30–60 s) and can **Rewatch** it.
4. **The forecast** always shows what's coming and what it takes, in the drill sergeant's voice: *"Saturday: 30 Biters and 2 Brutes. Brutes are armored, recruit. One more gym session buys the Rocketeers that crack them."*

A normal day in the app takes **1–2 minutes**.

## 3. Economy

One resource: **protein** (Roni, 3 Nov: a protein tub icon; the code and the save still call it `supplies`).

| Source | Protein | Rule |
|---|---|---|
| Gym session ("I went") | **100** | Flat: showing up is what counts. At most one a day |
| Comeback rally | **+100** | On the first session after 7+ days without one. Days on leave don't count as days away |
| Home card ("10 squats", Done) | **10** | At most one a day (≈ half a session a week at most). Honor system, tap Done |

| Spend | Cost |
|---|---|
| Tier 1 unit | 50 |
| Tier 2 unit | 100 |
| Tier 3 unit | 200 |
| Building level 2 | 150 |
| Building level 3 | 300 |

The **weekly target is 3 sessions**.

## 4. Attacks

- **Weekly boss: Saturday 20:00.** Sized from the 3-a-week target:

  | Sessions that week | Outcome |
  |---|---|
  | 3 | a comfortable win |
  | 2 | a coin flip |
  | 0–1 | a loss |

  The engine is tuned by simulation to hit those odds.
- **Daily raids**, every evening except Saturday, at 20:00: small. Your standing army and the HQ handle them. A lost raid only wounds units.
- **Rest days are safe**: nothing punishes a day you planned not to go.
- **Forecast:** on Home at all times, plus one notification mid-week (Wednesday 18:00). A second notification brings each battle's result. No other notifications.

## 5. Losing, leave and coming back

- **Nothing is ever wiped.** The HQ can be besieged, never destroyed.
- **Wounded, not dead.** Units that fall are out until your next gym session, then they're back.
- **A lost boss occupies one production building:** it can't train or upgrade until your next gym session liberates it.
- **"On leave" switch** (sick, a sick kid, an emergency): no raids, no boss, nothing decays. Turning it off and going to the gym gives the Comeback rally.

## 6. Battles

- **Auto-battle from a simulation**, then a playback; **Rewatch** replays the same battle.
- **Squads, not crowds (Roni, 3 Nov):** one sprite per unit type per side with a count badge ("×5", "×50"). The badge drops as units fall. The simulation fights squads: a squad's health is its count × the unit's health, and losses come off the count.
- **Ground only** in the first version.
- **Light counters**, three traits:

  | Trait | Strong against it |
  |---|---|
  | **Light** | ordinary weapons |
  | **Armored** | anti-armor weapons (×2) |
  | **Swarm** | splash weapons (×2, and one shot drops several) |

  The forecast names the week's main trait and what beats it.
- **Battlefield:** your base on the left, the attackers come from the right. The HQ fights too (it shoots a little).

## 7. Races

- **Built to grow:** a race is data (its buildings, units, stats, traits and art ids), so a new race is a new definition plus its art, never new game rules. No game code names a race.
- **Each race:** an **HQ** plus **2 production buildings** with **3 units each**. Building level 1/2/3 unlocks unit 1/2/3. Each level also gives that building's units +10% health and damage. Units first, buildings few: no towers, no walls.
- **First version:** you play **the Outpost** (humans). Season 1's attackers are **the Brood** (swarm), enemy only.
- **Later:** the Brood and the Radiant (tech aliens) become playable; you pick your race at the start; the others attack.

### The Outpost (numbers tuned by the balance test, Task 2)

| Building | Level | Unit | Cost | Health | Damage | Range | Speed | Trait | Special |
|---|---|---|---|---|---|---|---|---|---|
| Barracks | 1 | Rifleman | 50 | 40 | 6 / 0.8 s | 3 | 1.0 | Light | |
| Barracks | 2 | Medic | 100 | 45 | heals 6 / 1.0 s | 2.5 | 1.0 | Light | Heals infantry |
| Barracks | 3 | Rocketeer | 200 | 100 | 72 / 1.5 s | 3.5 | 0.9 | Light | ×2 vs Armored |
| Motor Pool | 1 | Buggy | 50 | 45 | 4 / 0.6 s | 2 | 2.2 | Light | Fast |
| Motor Pool | 2 | Tank | 100 | 150 | 18 / 2.25 s | 4.5 | 0.7 | Armored | Splash 4, ×2 vs Swarm |
| Motor Pool | 3 | Walker | 200 | 400 | 15 / 1.25 s | 2 | 0.5 | Armored | |
| HQ | | | | 500 | 5 / 1.0 s | 4 | | | Never falls: at 0 the battle is lost, the HQ is besieged |

**No overkill:** one shot drops at most one enemy unit, unless the weapon has splash (the Tank: up to 4). So a Rocketeer's rocket kills one Biter, not four: Rocketeers crack armor and are wasted on swarms, while Tanks shred swarms.

Value per protein is balanced on health × damage per second ÷ cost². In a squad fight, strength grows with the square of numbers, so cheap units count double.

### The Brood, as the season-1 enemy

| Unit | Weight | Health | Damage | Range | Speed | Trait |
|---|---|---|---|---|---|---|
| Biter | 15 | 18 | 1.2 / 0.8 s | melee | 1.8 | Swarm |
| Spitter | 30 | 24 | 6.3 / 1.4 s | 3 | 1.1 | Light |
| Brute | 120 | 240 | 11.5 / 1.6 s | melee | 0.7 | Armored |

**Waves:**
- **The weekly boss** has a budget of 240 + 265 per week after the first (week 12: ×1.3), spent on Brood units. Each battle draws one main trait, which gets 65% of the budget. Brutes join from week 2.
- **Raids** are 30% of that week's boss.

**Measured over whole seasons** (the reference player, 40 seasons each, `BalanceTest`):

| Pace | Boss win |
|---|---|
| 3 a week | ~85% |
| 2 a week | ~55% |
| 1 a week | ~5% |
| 0 | 0% |

**After a strong streak:**
- one week off → that boss ~45%;
- a second week off → ~10%;
- two weeks after coming back → ~65%.

**Warm-up:** a game started on any day but Sunday starts its season on the next Sunday. Sessions already count, and there are no battles before that.

The Brood's other three units (Burster, an ambusher, Behemoth) and the Radiant come later.

## 8. Seasons

- **12 weeks.** Waves grow week by week, and week 12 is the season's final boss.
- **At the end:**
  - a season recap (sessions, wins, the longest comeback);
  - a trophy;
  - one **permanent perk** you choose (e.g. +5% unit health).

  Then a new map, a new enemy, and building levels and units reset.

## 9. Voice

A **drill sergeant**: tough, short, a bit funny, never cruel about missed weeks. Examples:
- *"Two sessions by Saturday, recruit, or that wall falls."*
- *"You're back. The troops noticed. Rally bonus: +100."*
- After "I went": *"Good work, recruit. +100 protein."*
- *"On leave. Get well. The base will wait."*
- *"Lost the Motor Pool to the swarm. One gym session and we take it back."*

## 10. Look

- **Chunky cartoon:**
  - thick dark outlines;
  - 3/4 view;
  - a small palette per race;
  - one **team color** stripe on every Outpost unit and building.
- **Cutout animation:** each unit is a few separate pieces (body, weapon, muzzle flash) moved in code: bob, recoil, flash, wheels, treads, steps. The only frame swaps are two-pose moves (the yard's pull-ups and punches, the drill recruit).
- **The base is alive (0.3.1):** the HQ's radar turns, its windows flicker, its flag (with a dumbbell) gusts now and then; the Barracks door opens for a moment and a recruit peeks out; the Motor Pool's garage door rolls up to show a vehicle with its headlights on; smoke, a swinging crane hook.
- **Gym everywhere (0.3.1):** a training yard next to the HQ (a recruit doing pull-ups, another on the punching bag), a dumbbell rack by the Barracks, the sergeant's whistle and stopwatch, the protein tub.
- **Art pipeline:** the "GymCraft Sprites" canvas holds the designs; their SVGs are in `docs/art/` (each moving piece is a class: `bob`, `door`, `poseA`...). The planning agent turns them into Kotlin (`designsystem/sprites/content/*Art.kt`) and checks the result by rendering it.

## 11. Scope

Roni's own app first, built so it can go on the Play Store later. Solo: no accounts, no network, no ads. Separate from Blocks.

## 12. Home (Task 3, reworked in 0.3.1 with Roni's feedback, 3 Nov)

**The base is the screen.** From top to bottom:
1. **A sky strip:** the protein tub and the amount ("240 protein"), the debug time (debug builds), the **⋮ menu** (Going on leave / I'm back; debug: Sprite test, time +1 hour, +1 day, back to real time). The error card and the leave banner ("On leave. No raids, no boss." with **I'm back**) appear here when they apply.
2. **The scene**, taking all the room left:
   - back row: the production buildings, labelled **"Barracks 1"**, **"Motor Pool 2"** (name and level). Tap one for its sheet. An occupied one is dimmed with OCCUPIED;
   - front: the HQ and the training yard;
   - bottom: the army, one sprite per unit type, its count above ("×5") and its wounded below ("+2 hurt").
3. **A ground strip:**
   - the sergeant's portrait and bubble (§9). After a tap he answers in the bubble; tapping it brings back his forecast line;
   - "While you were away" when there are new battle results ("Raid Tue: held. 2 wounded."), **Got it** clears it;
   - **one line for the boss:** "BOSS Sat 20:00 · 30 Biters and 2 Brutes 62%";
   - **one line for the home card:** the drill recruit doing the move, "15 squats", and **+10** with the tub;
   - **the lift:** hold "HOLD: I WENT TO THE GYM" about a second; a barbell rises, locks out with a buzz, +100 protein, and the troops cheer (infantry raise a fist, every unit hops). Once a day, then "Logged today. Rest up, recruit."

**Home cards** and the drill recruit's move: 15 squats (squat), 10 push-ups (push-up), 30-second plank (push-up, held), 20 lunges (squat), 15 sit-ups (sit-up), 30 jumping jacks (jacks), 10 burpees (jacks), 20 glute bridges (sit-up), 45-second wall sit (squat, held), 12 chair dips (push-up). A held move shows its second pose with a little shake.

**Building sheet:** "Barracks 1", "Level 1 of 3"; each unit with its price and the tub ("50 each · 2 ready"), −/+ (1 to 20) and **Train 150**; locked units greyed ("Needs level 2"); **Upgrade to Barracks 2 · 150** (or "Fully upgraded"). An occupied building says so and trains nothing.

**Battles in 0.3.x** are fought when they fall due, each time the app opens or comes back to the front; the results show in "While you were away". **Task 4** gives them their own screen (the "An attack" board on the canvas): one lane, the HQ at the left edge, your squads, the Brood from the right, shots from each muzzle, both armies' strength on top, Pause, ×1, ×2, Skip.

**The sergeant's lines** (built from the forecast):
- "2 more sessions by Saturday, recruit, or that wall falls. Armored wave: Rocketeers crack it."
- "One more session tonight, recruit, or that wall falls." (on boss day)
- "Coming Saturday: 30 Biters and 2 Brutes. We're ready. Keep it that way."
- "Coming Saturday: 60 Biters and 4 Brutes. Too big for us yet. Every session still counts."
- "Season starts Sunday, recruit. Every session now is a head start." (warm-up)
- "On leave. Get well. The base will wait."
- "Good work, recruit. +100 protein." / "You're back. The troops noticed. Rally bonus: +100. +200 protein."
- "3 Riflemen ready." / "Barracks 2. Medics unlocked." / "Not enough protein. A gym session brings 100."


## 13. Upgrade cards (later; decided by Roni, 4 Nov)

- **A roguelike draft once a week:** after every boss battle, won or lost, three random cards are dealt and you pick one. Protein still buys units and building levels; the cards are the weekly extra. No boss (on leave), no draft.
- **No stat sponges.** No "+8% Rifleman health": every card changes what happens in a battle or how the week plays. Examples:
  - battlefield: rockets that go through to the unit behind; Buggies that pull back after each shot; a Tank that doubles its range while standing still; once per battle a unit that would fall stays up; Medics heal everyone once when the army drops below a third;
  - base: a minefield that drops the first squad to reach the middle; an HQ mortar; sandbags for the first five seconds;
  - gym: a free Rifleman for a session after a rest day; the whistle (a charge at double speed) for the first battle after a comeback; once a season a lost boss occupies nothing; a smaller boss in a week you beat your best session count;
  - legendary: a Brood unit fights for you in every boss battle; all three infantry types share one healing pool.
- **Scale:** about 80 to 100 mechanics with ranks I to III (drawing one you have offers the next rank) and per-race versions: 400+ offers. Rarities: common, rare, legendary.
- **Built to grow:** cards are data hung on a few engine hooks (battle start, shot, unit falls, session logged), per race, each with a test. The balance test plays seasons with random drafts, and waves grow a little with the cards you hold.
- **Season end:** the season's cards go with the buildings and units; the permanent perk (§8) stays.
