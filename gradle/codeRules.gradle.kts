// Code rules (GymCraft task 1): fail the build when a module's code breaks one of the rules below.
// Applied to every module; runs before every build, so the Play button catches it too.
// Each rule names the fix. No escape hatch on purpose.
val codeRules = tasks.register("codeRules") {
    group = "verification"
    description = "Fails on !!, GlobalScope, runBlocking, experimental opt-ins outside designsystem/, and race names outside content/."
    val base = layout.projectDirectory
    val sources = files(base.dir("src/main/java"), base.dir("src/main/kotlin")).asFileTree.matching { include("**/*.kt") }
    val root = base.asFile
    inputs.files(sources)
    val stamp = layout.buildDirectory.file("codeRules/ok")
    outputs.file(stamp)
    doLast {
        val strings = Regex("\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*'")
        // (pattern, allowed in designsystem/, fix). Checked on code with strings and comments removed.
        val codeRules = listOf(
            Triple(Regex("!!"), false, "no !! - use ?:, ?.let or an early return (architecture.md, Failures)"),
            Triple(Regex("\\bGlobalScope\\b"), false, "no GlobalScope - inject @ApplicationScope"),
            Triple(Regex("\\brunBlocking\\b"), false, "no runBlocking in app code - make the caller suspend"),
            Triple(Regex("@OptIn\\(|\\bExperimental\\w*Api\\b"), true, "experimental APIs only inside designsystem/ - wrap the component there"),
        )
        // Races are data: only files under a content/ folder may name one (strings included).
        val raceNames = Regex("\\b(outpost|brood|radiant)\\b", RegexOption.IGNORE_CASE)
        val problems = mutableListOf<String>()
        sources.files.sortedBy { it.path }.forEach { file ->
            val path = file.relativeTo(root).invariantSeparatorsPath
            val inDesignSystem = path.contains("/designsystem/")
            val inContent = path.contains("/content/")
            var inBlockComment = false
            file.readLines().forEachIndexed { index, raw ->
                var line = raw
                if (inBlockComment) {
                    val end = line.indexOf("*/")
                    if (end < 0) return@forEachIndexed
                    line = line.substring(end + 2); inBlockComment = false
                }
                val open = line.indexOf("/*")
                if (open >= 0) {
                    val close = line.indexOf("*/", open + 2)
                    if (close < 0) { inBlockComment = true; line = line.substring(0, open) }
                    else line = line.substring(0, open) + line.substring(close + 2)
                }
                val withStrings = line.substringBefore("//")
                val code = line.replace(strings, "\"\"").substringBefore("//")
                codeRules.forEach { (regex, designSystemOk, fix) ->
                    if ((!designSystemOk || !inDesignSystem) && regex.containsMatchIn(code)) {
                        problems += "$path:${index + 1}: $fix\n    ${raw.trim()}"
                    }
                }
                if (!inContent && raceNames.containsMatchIn(withStrings)) {
                    problems += "$path:${index + 1}: no race names outside content/ - read the race from its definition\n    ${raw.trim()}"
                }
            }
        }
        if (problems.isNotEmpty()) {
            throw GradleException("Code rules broken (${problems.size}):\n" + problems.joinToString("\n"))
        }
        stamp.get().asFile.apply { parentFile.mkdirs(); writeText("ok") }
    }
}
