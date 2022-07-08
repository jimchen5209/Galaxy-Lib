import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "one.oktw"
version = "1.0-SNAPSHOT"

val coroutinesVersion = "1.6.3"
val bsonVersion = "4.6.0"

repositories {
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", coroutinesVersion)
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", coroutinesVersion)
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-reactive", coroutinesVersion)
    api("org.mongodb", "bson", bsonVersion)

    shadow(kotlin("stdlib-jdk8"))
    shadow(kotlin("reflect"))
    shadow("org.jetbrains.kotlinx", "kotlinx-coroutines-core", coroutinesVersion)
    shadow("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", coroutinesVersion)
    shadow("org.jetbrains.kotlinx", "kotlinx-coroutines-reactive", coroutinesVersion)
    shadow("org.mongodb", "bson", bsonVersion)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

val shadowJar by tasks.getting(ShadowJar::class) {
    archiveClassifier.set("all")
}

val sourcesJar by tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.getByName<Jar>("jar") {
    dependsOn(sourcesJar)
    dependsOn(shadowJar)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
//            artifact(sourcesJar)
        }
    }
}
