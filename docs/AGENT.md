# Working agent instructions (GymCraft)

Read this first, every session. The current task is named in the next paragraph.

You're the working agent for GymCraft, an Android game (Kotlin, Compose, Hilt) in this repo. Implement **docs/Task-2.md** (target 0.2.0) exactly, on the branch `task-2/v0.2.0`. Task 2 runs only once v0.1.0 is tagged (check `git tag`); otherwise stop and say so. The owner's tokens are limited: be fast, decisive and brief.

These rules come from weeks of work on Roni's previous app. Every one of them is there because an agent lost hours to the opposite. Follow them literally.

START
0. Your session doesn't start in the repo. First: cd "C:/GymCraft". Every command below runs from there. If a command ever says "not a git repository", cd there again.
1. One command: git branch --show-current ; git log --oneline -8 2>$null ; git status --short
   - Commits named "task-2 slice N" are finished slices; continue with the lowest one missing.
   - Modified files mean a previous session stopped mid-slice: build that slice; if green or a quick fix, finish and commit it; otherwise git stash push -m "partial" and redo the slice.
2. Read docs/Task-2.md once, all of it. Read docs/architecture.md once (it's short; it's binding). Open docs/game-design.md only when the task points to a section. Don't read SUMMARY.md.

FINDING THINGS: GREP FIRST
- Search the repo with git grep -n "<name>" (add -- "*.kt" to narrow). It answers in a second.
- Never write Python or PowerShell scripts to search or to analyze code. Never read whole files to search. Never search the web for anything inside the repo.
- Use the web only for an Android API you can't find an example of, and only after git grep came up empty.
- Read a file once, around the lines you need (your Read tool with offset/limit). Don't re-read a file after editing it.
- Never decompile, disassemble or read the sources of a library (no javap, no unzipping jars) to "understand" a problem. An agent once spent most of a day disassembling the serialization library for a bug whose cause was one line in our own code. Look at our code, the task, and the error message.

SHELL: WINDOWS POWERSHELL 5.1
- No &&, ||, tail, head, grep, sed, cat <<. Separate commands with ; and trim output with | Select-Object -First N or -Last N. Stay in the repo root; don't cd anywhere else.
- Never write scratch or output files into the repo (an earlier repo's root filled with files named a43, c5, t8...). If you must save output, use $env:TEMP.
- Edit with your file tools. If you ever write a file from PowerShell, add -Encoding UTF8. "A?" in console output is just PowerShell showing a non-ASCII character.
- Don't check versions, the environment, or git between steps. The build works; the commit line shows the hash.

GRADLE: ALWAYS THIS FORM
   cmd /c "gradlew.bat <tasks> --offline --console=plain > %TEMP%\g.txt 2>&1" ; "exit=$LASTEXITCODE" ; Select-String -CaseSensitive -Path $env:TEMP\g.txt -Pattern '^e: |FAILED|What went wrong' -Context 0,3 | Select-Object -First 25
- <tasks>, by what changed:
  - only `:engine` → `:engine:test`;
  - `:app` → `:app:testDebugUnitTest` (or `:app:compileDebugKotlin` when the slice has no app tests);
  - the full gate → `:engine:test :app:testDebugUnitTest :app:assembleDebug`.
  - One test class: add `--tests <Class>` (no quotes).
- Output goes to a file through cmd, never through a PowerShell pipe. The Gradle daemon can hold a pipe open until the shell times out (that hang cost a whole session once), and PowerShell wraps Gradle's stderr in NativeCommandError noise.
- exit=0 is the whole answer. Lines starting with w: are warnings: never read them, never fix them.
- Why did a test fail? Read the result instead of rerunning:
   Select-String -Path engine\build\test-results\test\*.xml,app\build\test-results\testDebugUnitTest\*.xml -Pattern '<failure' -Context 1,4 | Select-Object -First 3
- One build per fix. If a build is still running after 10 minutes, it's stuck: cmd /c "gradlew.bat --stop", then run it once more.
- Always --offline.
- Never wait on anything external: no sleep loops, no polling, no waiting for a device. If adb devices doesn't list a phone, skip the phone checks.

READING A RESULT: ONE LOOK, ONE DECISION
- exit=0 means every test passed, including the ones you just added. Passing tests print nothing; that's normal. Don't check that they ran, don't rerun, move on.
- exit is not 0: fix what the error lines say. That's the only other case.
- A shell timeout or warning after BUILD SUCCESSFUL changes nothing: the build is green.
- Once a result is in, don't reopen it ("wait, but..."). Doubting a green build costs more than the rare miss the final gate would catch anyway.

EACH SLICE
1. Read the slice. git grep what you'll change.
2. Make the slice's edits, its code and its named tests. Paste the task's code and adapt names; don't rewrite it.
3. Build early. Never change more than about 8 files without compiling. An earlier agent changed dozens of files without compiling, and recovering the broken build took a whole extra task.
4. Build and test the slice; fix until exit=0. Then the full gate once.
5. git add -A ; git commit -m "task-2 slice N: <title>" 2>&1 | Select-Object -First 1
6. One line: "Slice N done: <hash>". Start the next slice at once.

THINKING: SHORT
- At most two sentences of thinking between commands. No long monologues weighing alternatives ("Hmm, but wait... actually... let me reconsider..."). Earlier agents burned hours this way: re-deriving how a test clock works, re-checking which store instance a test used, hunting for a bug path the planner had already said to stop hunting.
- If you notice you're on the third "actually" about the same thing, stop: take the simplest option that satisfies the task, note it in docs/NOTES.md, move on.
- Time-box: after ~15 minutes on one problem, do exactly that.
- Don't verify what the compiler or the tests verify. Warnings and unused imports are fine.

ALWAYS
- Keep going until the final report. Stop early only for a real blocker, and say exactly what it is. Don't ask questions; log each choice in docs/NOTES.md in one line.
- **Architecture is binding** (docs/architecture.md):
  - **State and ViewModels:** @Immutable State, sealed Action, sealed Effect; a @HiltViewModel with injected managers and one onAction(); state changed only via _state.update { it.copy(...) }; effects via MutableSharedFlow.
  - **Composables:** only the Route knows the ViewModel; every other composable takes (state, onAction). Never a list of callback parameters, never a global or static store.
  - **Engine:** rules live in :engine (pure Kotlin); races are data, and no code outside a content/ folder names a race.
  - **If the task seems to need otherwise, don't improvise:** say so in the report. (An earlier agent once drifted into callback-parameter lists because the planner's wording was loose; that is never OK.)
- **Code rules:** no !!, no GlobalScope, no runBlocking, experimental APIs only inside designsystem/. The build checks them; don't fight it, fix the code.
- **Tests:**
  - Add only the tests the task names.
  - No Compose UI tests, no Robolectric Compose tests, no screenshot tests, unless the task names one.
  - A failing test means the code is wrong: never change a test's expectations unless the task says so; list every test whose setup you changed.
- **Code marked verified or shipped by the task** (the task lists it; in Task 2: every file under engine/src) is final: don't edit it.
- **Real names win:** if a name in the task doesn't exist, use the real one. Don't refactor beyond the slice.
- **If an edit mangles a file:** git checkout -- <file>, then redo the edit.
- **Phone:**
  - **GymCraft:** use only com.roni.gymcraft.debug, the debug build.
  - **Blocks:** never touch Roni's Blocks apps at all: com.roni.blocks holds his real data, com.roni.blocks.debug is his test app. Don't install over them, clear them, force-stop or uninstall them.
- **Finish with the task's last slice:** final gate, merge into main, tag, push, the short final report, then **stop**. Don't re-verify finished slices or re-check old todo lists; mark them done and end.
