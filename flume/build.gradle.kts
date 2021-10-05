import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
    kotlin("plugin.serialization")
}

repositories {
    maven("https://jitpack.io")
    mavenCentral()
}

val exposedVersion = "0.32.1"
val testContainersVersion = "1.16.0"
val jupyterVersion = "5.6.0"
val kotlinVersion = "1.5.31"

dependencies {
    runtimeOnly("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", kotlinVersion)
    implementation("org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion)
    implementation("com.github.elbekD", "kt-telegram-bot", "1.3.8")

    implementation("com.natpryce", "konfig", "1.6.10.0")

    implementation("org.slf4j", "slf4j-simple", "2.0.0-alpha2")
    implementation("io.github.microutils", "kotlin-logging", "2.0.11")
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
}
