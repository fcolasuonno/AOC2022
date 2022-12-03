plugins {
    kotlin("jvm") version "1.7.22"
    application
}

repositories {
    mavenCentral()
}

application {
    project.properties["main"]?.toString()
        ?.substringAfterLast("src")
        ?.drop(1)
        ?.replace(".kt", "Kt")
        ?.replace('\\', '.')
        ?.replace('/', '.')
        ?.let {
            mainClass.set(it)
        }
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
