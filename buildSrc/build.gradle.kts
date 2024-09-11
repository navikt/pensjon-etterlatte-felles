import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "no.nav.etterlatte"
version = "unspecified"

repositories {
    gradlePluginPortal()
    maven(
        //name = "JCenter Gradle Plugins",
        url = "https://dl.bintray.com/gradle/gradle-plugins"
    )
}

dependencies {
    implementation(kotlin("gradle-plugin"))

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

tasks {
    withType<Wrapper> {
        gradleVersion = "8.10"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_21.toString()
    }
}
