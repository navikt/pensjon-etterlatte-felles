plugins {
    id("etterlatte.common")
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(Ktor.OkHttp)
    implementation(Ktor.ClientCore)
    implementation(Ktor.ClientLoggingJvm)
    implementation(Ktor.ClientAuth)
    implementation(Ktor.ClientJackson)
    implementation(NavFelles.RapidAndRivers)

    implementation(Etterlatte.Common)

    implementation("com.github.navikt:brukernotifikasjon-schemas:v2.5.1")
    implementation(Etterlatte.KtorClientAuth)
    implementation ("com.nfeld.jsonpathkt:jsonpathkt:2.0.0")
    implementation("io.confluent:kafka-avro-serializer:6.2.2") {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
    implementation("org.apache.kafka:kafka-clients"){
        version {
            strictly("2.8.1")
        }
    }
    implementation("no.nav:kafka-embedded-env:2.8.1")
    testImplementation(Ktor.ClientMock)
    testImplementation(MockK.MockK)
    testImplementation(Kafka.EmbeddedEnv)

    testImplementation(Etterlatte.CommonTest)
}
