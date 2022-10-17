package no.nav.etterlatte

import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.header
import io.ktor.server.request.path
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking
import no.nav.etterlatte.routes.aareg
import no.nav.etterlatte.routes.internal
import no.nav.etterlatte.routes.regoppslag
import no.nav.etterlatte.routes.inntektskomponenten
import org.slf4j.event.Level
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    val config = runBlocking { environment.config.load() }
    val stsClient = StsClient(config.sts)
    installAuthentication(config.aad, config.tokenX)

    install(ContentNegotiation) { jackson() }
    install(IgnoreTrailingSlash)

    install(CallLogging) {
        level = Level.INFO
        filter { call -> !call.request.path().startsWith("/internal") }
        mdc("correlation_id") { call -> call.request.header("x_correlation_id") ?: UUID.randomUUID().toString() }
    }

    routing {
        internal()
        authenticate("aad") {
            route("/aad") {
                inntektskomponenten(config, stsClient)
                aareg(config, stsClient)
                regoppslag(config, stsClient)
            }
        }
    }
}

