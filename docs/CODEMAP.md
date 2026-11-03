# GymCraft codemap (0.1.0)

One line per source file.

## :engine
- `engine/src/main/kotlin/com/roni/gymcraft/engine/model/Ids.kt` — value-class ids (RaceId, BuildingId, UnitId, TraitId, ArtId) for races-as-data.
- `engine/src/test/kotlin/com/roni/gymcraft/engine/model/IdsTest.kt` — `idsCompareByValue`.

## :app — build and shell
- `app/src/main/AndroidManifest.xml` — the single `.MainActivity`, `App`, dark theme, launcher icon, no permissions.
- `app/src/main/java/com/roni/gymcraft/App.kt` — `@HiltAndroidApp` Application.
- `app/src/main/java/com/roni/gymcraft/MainActivity.kt` — `@AndroidEntryPoint` activity; sets `GymCraftTheme { AppRoot() }`.
- `app/src/main/res/values/strings.xml` — `app_name`.
- `app/src/main/res/values/themes.xml` — `Theme.GymCraft`, dark window/status/navigation colors.
- `app/src/main/res/drawable/ic_launcher.xml` — placeholder vector: dark rounded square with an orange shield.

## :app — designsystem
- `app/src/main/java/com/roni/gymcraft/designsystem/Theme.kt` — dark Material 3 color scheme and `GymColors` for Canvas drawing.
- `app/src/main/java/com/roni/gymcraft/designsystem/sprites/CutoutSprites.kt` — builds the cutout part bitmaps (body, weapon, flash, stripe) and the badge layouts once; `rememberCutoutSprites()`.

## :app — navigation and screens
- `app/src/main/java/com/roni/gymcraft/navigation/AppRoot.kt` — Navigation 3 back stack: `HomeKey`, and `SpriteTestKey` in debug builds.
- `app/src/main/java/com/roni/gymcraft/feature/home/Home.kt` — Home state/action/effect, `HomeViewModel`, route and screen with the title and the "Sprite test" button.
- `app/src/main/java/com/roni/gymcraft/feature/spritetest/SpriteTest.kt` — sprite-test state/action, `SpriteTestViewModel`, route, screen and the `Battlefield` canvas.
- `app/src/main/java/com/roni/gymcraft/feature/spritetest/SpriteWorld.kt` — preallocated squad arrays; step (walk, bob, recoil, flash) and allocation-free draw.
- `app/src/main/java/com/roni/gymcraft/feature/spritetest/FpsMeter.kt` — counts frames and the worst frame gap per second.

## :app — tests
- `app/src/test/java/com/roni/gymcraft/feature/spritetest/FpsMeterTest.kt` — `oneSecondOf60Frames`.
