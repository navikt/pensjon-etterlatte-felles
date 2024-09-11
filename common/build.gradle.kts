import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib"))
    api(kotlin("reflect"))

    api(libs.jacksonDatatypejsr310)
    api(libs.jackson.datatypejdk8)
    api(libs.jackson.modulekotlin)

    testImplementation(libs.jupiter.api)
    testImplementation(libs.test.kotest.assertionscore)
    testImplementation(libs.mockk)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/pensjon-etterlatte-felles")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {

            pom {
                name.set("common")
                url.set("https://github.com/navikt/pensjon-etterlatte-felles")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/navikt/pensjon-etterlatte-felles.git")
                    developerConnection.set("scm:git:https://github.com/navikt/pensjon-etterlatte-felles.git")
                    url.set("https://github.com/navikt/pensjon-etterlatte-felles")
                }
            }
            from(components["java"])
        }
    }
}
