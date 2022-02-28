import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20-M1"
    kotlin("plugin.serialization") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.cancelcloud"
version = "1.0.1"

val exposedVersion: String by project
var host = "github.com/cancel-cloud/InvSync"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
}



dependencies {
    //Purpur
    compileOnly("org.purpurmc.purpur", "purpur-api", "1.18.1-R0.1-SNAPSHOT")

    //Jetbrains-Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.processResources {
    expand("version" to project.version, "name" to project.name, "website" to "https://$host")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveClassifier.set("Runnable")
    dependencies {
        include(dependency("org.jetbrains.exposed:exposed-core:$exposedVersion"))
        include(dependency("org.jetbrains.exposed:dao:$exposedVersion"))
        include(dependency("org.jetbrains.exposed:exposed-jdbc:$exposedVersion"))
    }
}