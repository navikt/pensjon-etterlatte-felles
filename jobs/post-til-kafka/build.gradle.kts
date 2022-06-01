plugins {
    id("etterlatte.common")
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    api(kotlin("reflect"))

    implementation(Logging.LogbackClassic)
    implementation(Logging.LogstashLogbackEncoder) {
        exclude("com.fasterxml.jackson.core")
        exclude("com.fasterxml.jackson.dataformat")
    }

    implementation(Kafka.Clients)
    implementation(Jackson.Core)
    implementation(Jackson.Databind)
    implementation(Jackson.ModuleKotlin)
    implementation(Jackson.DatatypeJsr310)

    testImplementation(MockK.MockK)
    testImplementation(Kafka.EmbeddedEnv)
}
