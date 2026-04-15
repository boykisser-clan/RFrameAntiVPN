plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
}

version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.minebench.de")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.0.0")
    annotationProcessor("com.velocitypowered:velocity-api:3.0.0")
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