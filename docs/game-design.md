# GymCraft: game design

Decided by Roni with the planning agent, 3 Nov 2026. **Binding**: a task may only build what is written here. Anything new goes back to Roni first, with pros and cons.

## 1. What it is for

GymCraft keeps Roni going to the gym, and above all brings him **back** after a missed week or two (his own or his kid's illness, emergencies). Most streak apps punish exactly then; GymCraft rewards returning.

A small base-builder in a chunky cartoon style inspired by StarCraft: Brood War. All races, units and names are **original**: inspired by, never copied (it may go on the Play Store later).

## 2. The loop

1. **Go to the gym → tap "I went"** (manual; no location, no permission). +100 supplies.
2. **Spend supplies:** train units, upgrade production buildings.
3. **Attacks come on a schedule.** The battle runs by itself; you watch it as a short animation (30–60 s) and can **Rewatch** it.
4. **The forecast** always shows what's coming and what it takes, in the drill sergeant's voice: *"Saturday: 30 Biters and 2 Brutes. Brutes are armored, recruit. One more gym session buys the Rocketeers that crack them."*

A normal day in the app takes **1–2 minutes**.

## 3. Economy

One resource: **supplies**.

| Source | Supplies | Rule |
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
  | **Swarm** | splash weapons (×1.5) |

  The forecast names the week's main trait and what beats it.
- **Battlefield:** your base on the left, the attackers come from the right. The HQ fights too (it shoots a little).

## 7. Races

- **Built to grow:** a race is data (its buildings, units, stats, traits and art ids), so a new race is a new definition plus its art, never new game rules. No game code names a race.
- **Each race:** an **HQ** plus **2 production buildings** with **3 units each**. Building level 1/2/3 unlocks unit 1/2/3. Each level also gives that building's units +10% health and damage. Units first, buildings few: no towers, no walls.
- **First version:** you play **the Outpost** (humans). Season 1's attackers are **the Brood** (swarm), enemy only.
- **Later:** the Brood and the Radiant (tech aliens) become playable; you pick your race at the start; the others attack.

### The Outpost (first numbers; the simulation will tune them)

| Building | Level | Unit | Cost | Health | Damage | Range | Speed | Trait | Special |
|---|---|---|---|---|---|---|---|---|---|
| Barracks | 1 | Rifleman | 50 | 40 | 6 / 0.8 s | 4 | 1.0 | Light | |
| Barracks | 2 | Medic | 100 | 45 | heals 5/s | 3 | 1.0 | Light | Heals infantry |
| Barracks | 3 | Rocketeer | 200 | 45 | 22 / 2.0 s | 5 | 0.9 | Light | ×2 vs Armored |
| Motor Pool | 1 | Buggy | 50 | 60 | 5 / 0.5 s | 3 | 2.2 | Light | Fast |
| Motor Pool | 2 | Tank | 100 | 160 | 18 / 2.2 s | 6 | 0.7 | Armored | Splash (×1.5 vs Swarm) |
| Motor Pool | 3 | Walker | 200 | 260 | 14 / 1.2 s | 4 | 0.5 | Armored | |
| HQ | | | | 1000 | 8 / 1.0 s | 6 | | | Never falls: at 0 the battle is lost, the HQ is besieged |

### The Brood, as the season-1 enemy

| Unit | Health | Damage | Range | Speed | Trait |
|---|---|---|---|---|---|
| Biter | 22 | 4 / 0.6 s | melee | 1.6 | Swarm |
| Spitter | 35 | 8 / 1.4 s | 4 | 1.0 | Light |
| Brute | 220 | 20 / 1.6 s | melee | 0.6 | Armored |

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
- *"On leave. Get well. The base will wait."*
- *"Lost the Motor Pool to the swarm. One gym session and we take it back."*

## 10. Look

- **Chunky cartoon:**
  - thick dark outlines;
  - 3/4 view;
  - a small palette per race;
  - one **team color** stripe on every Outpost unit and building.
- **Cutout animation:** each unit is a few separate pieces (body, weapon, muzzle flash) moved in code: bob, recoil, flash, wheels, treads, steps. No frame-by-frame art.
- **Art pipeline:** AI-generated sprites, cleaned up and cut into those pieces. The first designs are on the "GymCraft Sprites" canvas.

## 11. Scope

Roni's own app first, built so it can go on the Play Store later. Solo: no accounts, no network, no ads. Separate from Blocks.
