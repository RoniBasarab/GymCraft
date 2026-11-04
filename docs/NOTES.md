# Notes (0.3.1)

One line per fix from slice 1 (planner-written files compiled here) and per choice made while building task 3.1.

- `HomeScreen.StripRow`: its content lambda had no `RowScope`, so `Modifier.weight` didn't resolve; made it `@Composable RowScope.() -> Unit` (layout unchanged).
- `HomeScreen` imports: removed the `androidx.compose.foundation.layout.weight` import (it names Compose's internal property) and added the missing `RowScope` import.
- `SergeantLine.result`: the bundle brought back the cross-module smart cast on `outcome.occupied`; bound it to a local `val occupied` again (same behavior; noted in 0.3.0 too).
- `:app:clean` was needed once: a mid-build daemon stop left stale Kotlin incremental state and thousands of false "unresolved" errors in the sprites package.

## 0.3.0

- `SergeantLine.result`: `outcome.occupied` is a property from `:engine`, so it can't be smart-cast; bound it to a local `val occupied` (same behavior).

## 0.2.0
- Git had no identity, so the repo uses `Roni Basarab <RoniBasarab@users.noreply.github.com>` locally.
- `local.properties` (untracked) points at `C:\Users\ronib\AppData\Local\Android\Sdk`; no SDK env var was set.
- Navigation 3 `entry<T>` is a member of `entryProvider`'s scope, so no `entry` import.
- Home effects are emitted with `viewModelScope.launch { _effects.emit(...) }`.
- Sprite test has no Effect: it has no one-off events (navigation, snackbar).
- Badge `TextLayoutResult`s for ×1…×9 are measured once in `rememberCutoutSprites()` and carried in `CutoutSprites`.
- Squad positions are fractions of the screen so a resize needs no new arrays.
- `SpriteWorld.step` clamps `dt` to 0.1 s so a paused frame doesn't teleport squads.
- `FpsMeter` rounds fps to nearest and the worst gap to nearest ms (16.67 ms → 17 ms).
