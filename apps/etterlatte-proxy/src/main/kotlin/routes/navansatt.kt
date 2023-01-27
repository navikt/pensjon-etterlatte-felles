package no.nav.etterlatte.routes

import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import no.nav.etterlatte.Config
import no.nav.etterlatte.NavCallId
import no.nav.etterlatte.NavConsumerId
import no.nav.etterlatte.httpClient
import no.nav.etterlatte.pipeRequest
import no.nav.etterlatte.pipeResponse
import org.slf4j.LoggerFactory
import java.util.UUID

fun Route.navansatt(config: Config) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    route("/navansatt") {
        val httpClient = httpClient()
        val dokUrl = config.navansatt.url

        get("{ident}") {
            val callId = call.request.header(HttpHeaders.NavCallId) ?: UUID.randomUUID().toString()
            val ident = call.parameters["ident"]!!

            try {
                val response = httpClient.get("$dokUrl/navansatt/$ident") {
                    header(HttpHeaders.NavConsumerId, "barnepensjon")
                    header(HttpHeaders.NavCallId, callId)
                    pipeRequest(call)
                }
                call.pipeResponse(response)
            } catch (cause: ResponseException) {
                logger.error("Feil i kall mot navansatt: ", cause)
                call.pipeResponse(cause.response)
            } catch (cause: Throwable) {
                logger.error("Feil i kall mot navansatt: ", cause)
                call.respondText(status = HttpStatusCode.InternalServerError) { cause.message ?: "Intern feil" }
            }
        }
    }
}