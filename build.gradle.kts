plugins {
    kotlin("jvm") version "1.7.22"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set(project.property("day")?.let { "day$it.CodeKt" })
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}
