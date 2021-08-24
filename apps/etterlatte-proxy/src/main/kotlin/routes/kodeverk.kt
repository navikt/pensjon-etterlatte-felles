package no.nav.etterlatte.routes


import io.ktor.application.call
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.request.header
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import no.nav.etterlatte.Config
import no.nav.etterlatte.NavCallId
import no.nav.etterlatte.NavConsumerId
import no.nav.etterlatte.StsClient
import no.nav.etterlatte.httpClient
import no.nav.etterlatte.pipeRequest
import no.nav.etterlatte.pipeResponse
import org.slf4j.LoggerFactory
import java.util.*

fun Route.kodeverk(config: Config, stsClient: StsClient) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    route("/kodeverk") {
        val httpClient = httpClient()
        val kodeverkUrl = config.kodeverk.url

        get {
            val callId = call.request.header(HttpHeaders.NavCallId) ?: UUID.randomUUID().toString()

            try {
                val response = httpClient.get<HttpResponse>(kodeverkUrl) {
                    header(HttpHeaders.NavConsumerId, "barnepensjon")
                    header(HttpHeaders.NavCallId, callId)
                    pipeRequest(call)
                }
                call.pipeResponse(response)
            } catch (cause: Throwable) {
                logger.error("Feil i kall mot Kodeverk: $cause")
                cause.printStackTrace()
            }
        }
    }
}