plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
}

version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.aikar.co/nexus/content/groups/aikar")
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.20-R0.2")
    implementation(project(":common"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.named("build") {
    dependsOn("shadowJar")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName.set("RFrameAntiVPN-${project.version}.jar")
}