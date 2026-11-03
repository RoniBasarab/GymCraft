import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// The game's rules (architecture.md): pure Kotlin, no Android. Tests are plain JVM JUnit.
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

apply(from = rootProject.file("gradle/codeRules.gradle.kts"))
tasks.named("compileKotlin") { dependsOn("codeRules") }

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
}
