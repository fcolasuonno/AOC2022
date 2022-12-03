plugins {
    kotlin("jvm") version "1.7.22"
    application
}

repositories {
    mavenCentral()
}

application {
    project.property("main")?.let {
        it.toString()
            .substringAfterLast("src")
            .drop(1)
            .replace(".kt", "Kt")
            .replace('\\', '.')
            .replace('/', '.')
            .also { System.err.println(it) }
    }?.let {
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
