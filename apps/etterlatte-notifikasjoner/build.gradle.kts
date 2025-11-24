plugins {
    id("etterlatte.common")
}

dependencies {
    implementation(libs.rapidAndRivers)

    implementation(libs.brukernotifikasjonerKotlinBuilder ) {
        exclude("org.apache.commons", "commons-compress")
    }

    implementation(libs.commons.compress)
    implementation(libs.kafka.avro.serializer) {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }

    testImplementation(libs.mockk)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.test.navfelles.rapidsandriversktor)
}
