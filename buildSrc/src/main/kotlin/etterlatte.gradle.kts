import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.logging.TestLogEvent

val libs = the<LibrariesForLibs>()

plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}
