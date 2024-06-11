package no.nav.etterlatte

import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.header
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking
import no.nav.etterlatte.auth.installAuthentication
import no.nav.etterlatte.config.SimuleringOppdragConfig
import no.nav.etterlatte.config.TilbakekrevingConfig
import no.nav.etterlatte.config.load
import no.nav.etterlatte.routes.internalRoute
import no.nav.etterlatte.routes.simuleringOppdragRoute
import no.nav.etterlatte.routes.tilbakekrevingRoute
import org.slf4j.event.Level
import java.util.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain
        .main(args)

fun Application.module() {
    val config = runBlocking { environment.config.load() }

    installAuthentication(config.aad)
    install(ContentNegotiation) { jackson() }
    install(IgnoreTrailingSlash)
    install(CallLogging) {
        level = Level.INFO
        filter { call -> !call.request.path().startsWith("/internal") }
        format { call ->
            skjulAllePotensielleFnr(
                "<- ${call.response.status()?.value} ${call.request.httpMethod.value} ${call.request.path()}"
            )
        }
        mdc("correlation_id") { call -> call.request.header("x_correlation_id") ?: UUID.randomUUID().toString() }
    }

    routing {
        internalRoute()

        authenticate("aad") {
            route("/aad") {
                tilbakekrevingRoute(TilbakekrevingConfig(config, false).createTilbakekrevingService())
                simuleringOppdragRoute(SimuleringOppdragConfig(config, true).createSimuleringOppdragService())
            }
        }
    }
}

/**
 * Bruker en regex med negativ lookbehind (?<!) og negativ lookahead (?!) for å matche alle forekomster av
 * nøyaktig 11 tall på rad ([ikke tall før, 11 tall, ikke tall etter] er tolkningen til regex'en), og bytte de
 * ut med 11 *. Ser ikke på "gyldigheten" til det som er potensielle fnr, bare fjerner alle slike forekomster.
 */
fun skjulAllePotensielleFnr(url: String): String = url.replace(Regex("(?<!\\d)\\d{11}(?!\\d)"), "***********")