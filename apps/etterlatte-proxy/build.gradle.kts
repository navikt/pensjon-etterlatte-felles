plugins {
    id("etterlatte.common")
}

dependencies {
    implementation(Ktor.ServerAuth)
    implementation(Ktor.ClientCore)
    implementation(Ktor.CallLogging)
    implementation(Ktor.ClientApache)
    implementation(Ktor.ClientAuth)
    implementation(Ktor.ClientLogging)
    implementation(Ktor.Jackson)
    implementation(Ktor.ServerContentNegotiation)
    implementation(Ktor.ClientContentNegotiation)
    implementation(Ktor.ServerCore)
    implementation(Ktor.ServerNetty)
    implementation(Ktor.ServerAuthJwt)

    testImplementation(NavFelles.MockOauth2Server)

    testImplementation(Ktor.ServerTests)

    implementation(Micrometer.Prometheus)
    implementation("org.json:json:20180813")
}
