plugins {
    id("etterlatte.common")
}

val cxfVersion = "4.0.2"

repositories {
    maven("https://jitpack.io")

    // org.apache.cxf:cxf-rt-ws-security:4.0.0 er avhengig av opensaml-xacml-saml-impl:4.2.0
    // som i skrivende stund ikke er tilgjengelig på maven central, men i shibboleth
    maven("https://build.shibboleth.net/maven/releases/")
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
    implementation(Ktor.OkHttp)
    implementation(NavFelles.NavFellesTokenClientCore)
    implementation(NavFelles.TjenestespesifikasjonerTilbakekreving)
    implementation(Cxf.CxfLogging)
    implementation(Cxf.CxfJaxWs)
    implementation(Cxf.CxfTransportsHttp)
    implementation(Cxf.CxfWsSecurity)
    implementation(Micrometer.Prometheus)
    implementation(Jackson.jacksonDatatypejsr310)

    testImplementation(NavFelles.MockOauth2Server)
    testImplementation(Ktor.ServerTests)
}
