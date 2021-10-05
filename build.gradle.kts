plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"

    `java-library`
    `maven-publish`
    signing
}

group = "org.pool-party"
version = "0.0.1"
