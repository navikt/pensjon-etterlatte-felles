object NavFelles {
    private const val navFellesToken = "3.0.11"
    const val RapidAndRivers = "com.github.navikt:rapids-and-rivers:2022100711511665136276.49acbaae4ed4"
    const val BrukernotifikasjonSchemas = "com.github.navikt:brukernotifikasjon-schemas:v2.5.2"
    const val MockOauth2Server = "no.nav.security:mock-oauth2-server:0.5.4"
    const val NavFellesTokenClientCore = "no.nav.security:token-client-core:$navFellesToken"
}

object Etterlatte {
    const val Common = "no.nav.etterlatte:common:2023.03.28-16.21.c37f68b0da69"
    const val CommonTest = "no.nav.etterlatte:common-test:2022.09.27-14.10.182856a1fda5"
    const val KtorClientAuth = "no.nav.etterlatte:ktor-client-auth:2022.09.28-10.09.cce630926582"
}

object Ktor {
    private const val version = "2.1.1"

    const val CallLogging = "io.ktor:ktor-server-call-logging:$version"
    const val ClientCore = "io.ktor:ktor-client-core:$version"
    const val ClientApache = "io.ktor:ktor-client-apache:$version"
    const val ClientAuth = "io.ktor:ktor-client-auth:$version"
    const val ClientContentNegotiation = "io.ktor:ktor-client-content-negotiation:$version"
    const val ClientLogging = "io.ktor:ktor-client-logging:$version"
    const val Jackson = "io.ktor:ktor-serialization-jackson:$version"
    const val ServerAuth = "io.ktor:ktor-server-auth:$version"
    const val ServerAuthJwt = "io.ktor:ktor-server-auth-jwt:$version"
    const val ServerCore = "io.ktor:ktor-server-core:$version"
    const val ServerContentNegotiation = "io.ktor:ktor-server-content-negotiation:$version"
    const val ServerNetty = "io.ktor:ktor-server-netty:$version"
    const val OkHttp = "io.ktor:ktor-client-okhttp:$version"
    const val ServerTests = "io.ktor:ktor-server-tests:$version"
}

object Jackson {
    const val jacksonDatatypejsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.0"
}

object Kafka {
    const val Clients = "org.apache.kafka:kafka-clients:3.2.3"
    const val AvroSerializer = "io.confluent:kafka-avro-serializer:6.2.2"
    const val TestContainer = "org.testcontainers:kafka:1.17.3"
}

object Jupiter {
    private const val version = "5.9.1"

    const val Api = "org.junit.jupiter:junit-jupiter-api:$version"
    const val Params = "org.junit.jupiter:junit-jupiter-params:$version"
    const val Engine = "org.junit.jupiter:junit-jupiter-engine:$version"
}

object Logging {
    const val Slf4jApi = "org.slf4j:slf4j-api:2.0.2"
    const val LogbackClassic = "ch.qos.logback:logback-classic:1.4.1"
    const val LogstashLogbackEncoder = "net.logstash.logback:logstash-logback-encoder:7.2"
}

object Micrometer {
    const val Prometheus = "io.micrometer:micrometer-registry-prometheus:1.9.4"
}

object MockK {
    const val MockK = "io.mockk:mockk:1.13.1"
}

object Kotest {
    private const val version = "5.4.2"

    const val AssertionsCore = "io.kotest:kotest-assertions-core:$version"
}
