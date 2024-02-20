plugins {
    id("etterlatte.common")
}

dependencies {
    implementation(libs.ktor.jackson)
    implementation(libs.rapidAndRivers)

    implementation(libs.etterlatte.common)
    implementation(libs.etterlatte.ktorClientAuth)

    implementation(libs.brukernotifikasjonSchemas) {
        exclude("org.apache.commons", "commons-compress")
    }
    implementation(libs.commons.compress)
    implementation(libs.kafka.avro.serializer) {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
    implementation(libs.kafka.clients)

    testImplementation(libs.mockk)
    testImplementation(libs.etterlatte.commonTest)
}
