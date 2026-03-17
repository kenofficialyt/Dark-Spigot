pluginManagement {
    repositories {
        maven {
            name = "papermc"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("io.papermc.paperweight.patcher") version "2.0.0-beta.19"
}

val minecraftVersion: String by properties
val paperVersion: String by properties
val parchmentVersion: String by properties

group = "tht.darkspigot"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

paperweight {
    server = project(":Paper-Server")

    mappings {
        parchment {
            minecraftVersion.set(minecraftVersion)
            parchmentVersion.set(parchmentVersion)
        }
    }

    tasks {
        register<Copy>("copyPaperServerDeps") {
        }
    }
}

subprojects {
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
        maven {
            name = "papermc"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}

tasks.register<Jar>("jar") {
    archiveFileName.set("DarkSpigot-${version}.jar")
    from(rootProject.file("src/main/java"))
    from(rootProject.file("src/main/resources"))
}
