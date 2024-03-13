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
    implementation(libs.ktor.clientCore)
    implementation(libs.ktor.callLogging)
    implementation(libs.ktor.clientApache)
    implementation(libs.ktor.clientAuth)
    implementation(libs.ktor.clientLogging)
    implementation(libs.ktor.jackson)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.clientContentNegotiation)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverAuthJwt)
    implementation(libs.ktor.okHttp)
    implementation(libs.navFellesTokenClientCore)
    implementation(libs.tjenestespesifikasjonerTilbakekreving)
    implementation(libs.cxf.logging)
    implementation(libs.cxf.jax.ws)
    implementation(libs.cxf.transports.http)
    implementation(libs.cxf.ws.security) {
        exclude("com.google.guava:guava")
        exclude("org.bouncycastle:bcpkix-jdk18on")
        exclude("org.eclipse.angus:angus-core")
        exclude("org.eclipse.angus:angus-mail")
    }
    implementation(libs.micrometer.prometheus)
    implementation(libs.jacksonDatatypejsr310)

    testImplementation(libs.mockOauth2Server)
    testImplementation(libs.ktor.serverTests)

    // Avhengigheter fra patching av sårbarheter i Apache CXF.
    // Vi bør kunne ta bort alle disse og exclude-lista for neste CXF-versjon
    implementation(libs.guava)
    implementation(libs.bcpkix)
    implementation(libs.angus.core)
    implementation(libs.angus.mail)
}
