import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

group = "dev.narcos"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://jitpack.io")
    maven(url = "https://repo.runelite.net")
}

dependencies {
    implementation("io.guthix:jagex-bytebuf-extensions:0.2.0")
    implementation("io.guthix:jagex-bytebuf-wrapper:0.2.0")
    implementation("net.runelite:cache:1.8.9")
    implementation("com.uchuhimo:konf:1.1.2")
    implementation("io.insert-koin:koin-core:3.2.0")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("io.github.classgraph:classgraph:4.8.146")
    implementation("org.json:json:20220320")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation(kotlin("reflect"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}