import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
    kotlin("plugin.serialization")
}

repositories {
    maven("https://jitpack.io")
    mavenCentral()
    flatDir {
        dirs = mutableSetOf(file("dependencies"))
    }
}

val exposedVersion = "0.32.1"
val testContainersVersion = "1.16.0"
val jupyterVersion = "5.6.0"
val kotlinVersion = "1.5.31"

dependencies {
    implementation(project(":flume"))
    runtimeOnly("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", kotlinVersion)
    implementation("org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion)
    implementation("com.github.elbekD", "kt-telegram-bot", "1.3.8")

    implementation("org.jetbrains.kotlin", "kotlin-test-junit5", kotlinVersion)
    implementation("org.junit.jupiter", "junit-jupiter-api", jupyterVersion)
    runtimeOnly("org.junit.jupiter", "junit-jupiter-engine", jupyterVersion)

    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jodatime", exposedVersion)

    implementation("org.flywaydb", "flyway-core", "7.12.0")

    implementation("io.mockk", "mockk", "1.12.0")
    implementation("org.testcontainers", "postgresql", testContainersVersion)
    implementation("org.testcontainers", "junit-jupiter", testContainersVersion)
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.DelicateCoroutinesApi"
}
