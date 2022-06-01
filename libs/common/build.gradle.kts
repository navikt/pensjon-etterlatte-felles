import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

group = "com.github.navikt.etterlatte"

dependencies {
    api(kotlin("stdlib"))
    api(kotlin("reflect"))

    api(Jackson.DatatypeJsr310)
    api(Jackson.DatatypeJdk8)
    api(Jackson.ModuleKotlin)

    implementation(Ktor.ClientCore)
    implementation(Ktor.ClientLoggingJvm)
    implementation(Ktor.ClientAuth)
    implementation(Ktor.ClientJackson)

    testImplementation(Jupiter.Api)
    testImplementation(Jupiter.Params)
    testRuntimeOnly(Jupiter.Engine)
    testImplementation(Kotest.AssertionsCore)
    testImplementation(MockK.MockK)
    testImplementation(Ktor.ClientMock)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "16"
    }
}

val githubUser: String? by project
val githubPassword: String? by project

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/pensjon-etterlatte-felles")
            credentials {
                username = githubUser
                password = githubPassword
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {

            pom {
                groupId = "com.github.navikt.etterlatte"
                artifactId = "etterlatte-common"

                name.set("pensjon-etterlatte-felles")
                description.set("Pensjon Etterlatte Felles")
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
