plugins {
    id("etterlatte.common")
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(Ktor.Jackson)
    implementation(NavFelles.RapidAndRivers)

    implementation(Etterlatte.Common)
    implementation(Etterlatte.KtorClientAuth)

    implementation(NavFelles.BrukernotifikasjonSchemas)
    implementation(Kafka.AvroSerializer) {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
    implementation(Kafka.Clients)

    testImplementation(Kafka.TestContainer)
    testImplementation(MockK.MockK)
    testImplementation(Etterlatte.CommonTest)
}
