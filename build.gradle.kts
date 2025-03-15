plugins {
    kotlin("jvm") version "2.0.20-Beta2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.github.keritial"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/")

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/" )

    maven("https://repo.opencollab.dev/main/")

    maven("https://repo.dmulloy2.net/repository/public/")
}

//val ktorVersion = "3.1.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.10")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    implementation("io.javalin:javalin:6.4.0")


//    implementation("io.ktor:ktor-server-core-jvm:3.1.1")
//    implementation("io.ktor:ktor-server-netty:3.1.1")
//    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")

    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")

    compileOnly("org.geysermc.geyser:api:2.4.0-SNAPSHOT")

    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")

//    implementation("io.ktor:ktor-client-core:$ktorVersion")
//    implementation("io.ktor:ktor-client-cio:$ktorVersion")
//    implementation("xyz.jpenilla:reflection-remapper:0.1.1")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

//java {
//    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
//}

tasks {
    build {
        dependsOn("shadowJar")
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

}

