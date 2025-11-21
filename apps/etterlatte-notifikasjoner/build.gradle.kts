import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

plugins {
    id("etterlatte.common")
}

dependencies {
    implementation(libs.rapidAndRivers)

    implementation(libs.brukernotifikasjonerKotlinBuilder ) {
        exclude("org.apache.commons", "commons-compress")
    }

    implementation(libs.commons.compress)

    testImplementation(libs.mockk)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.test.navfelles.rapidsandriversktor)
}
