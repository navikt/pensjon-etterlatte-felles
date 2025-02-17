plugins {
    id("etterlatte.common")
}

repositories {
    // org.apache.cxf:cxf-rt-ws-security:4.0.2 er avhengig av opensaml-xacml-saml-impl:4.2.0
    // som i skrivende stund ikke er tilgjengelig på maven central, men i shibboleth
    maven("https://build.shibboleth.net/maven/releases/")
}

dependencies {
    implementation(libs.ktor.serverAuth)
    implementation(libs.ktor.callLogging)
    implementation(libs.ktor.clientApache)
    implementation(libs.ktor.clientLogging)
    implementation(libs.ktor.jackson)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.clientContentNegotiation)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverAuthJwt)
    implementation(libs.tjenestespesifikasjonerTilbakekreving)
    implementation(libs.tjenestespesifikasjonerOppdragSimulering)
    implementation(libs.cxf.logging)
    implementation(libs.cxf.jax.ws)
    implementation(libs.cxf.transports.http)
    implementation(libs.cxf.ws.security)
    implementation(libs.micrometer.prometheus)

    testImplementation(libs.mockOauth2Server)
    testImplementation(libs.ktor.serverTests)
}
