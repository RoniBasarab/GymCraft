# Manual tests

## Check on the phone (0.3.0)

Debug build (`com.roni.gymcraft.debug`):

1. Home opens: **100** supplies; the HQ, Barracks Lv 1, Motor Pool Lv 1; "No troops yet"; the sergeant's line; the boss forecast card.
2. Hold **I WENT**: the bar fills, the phone buzzes, **200** supplies, the bubble says "Good work, recruit. +100 supplies.", the button now says "Logged today". Tap the bubble: back to his forecast line.
3. Home card **Done +10**: **210**, the button greys out.
4. Tap the **Barracks**: Rifleman available, Medic and Rocketeer greyed ("Needs level 2", "Needs level 3"). Pick 3 with +, **Train · 150**: "3 Riflemen ready.", and a Rifleman with ×3 on the base.
5. Menu → **Going on leave**: the banner, no forecast. **I'm back**: the banner goes.
6. Menu → **Debug: time +1 day** (and +1 hour) until the game's time, top right, is past 20:00 on or after the first Sunday (a game started mid-week warms up until Sunday): "While you were away" lists the raids; **Got it** clears it.
7. Close the app from recents and open it again: the same supplies, army and buildings.
