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
import no.nav.etterlatte.config.Config
import no.nav.etterlatte.auth.sts.StsClient
import org.slf4j.LoggerFactory
import java.util.UUID

fun Route.aaregRoute(config: Config, stsClient: StsClient) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    route("/aareg/arbeidstaker/arbeidsforhold") {
        val httpClient = httpClient()
        val url = config.aaregUrl + "/arbeidstaker/arbeidsforhold"

        get {
            val stsToken = stsClient.getToken()
            val callId = call.request.header(HttpHeaders.NavCallId) ?: UUID.randomUUID().toString()
            val personIdent = call.request.header("Nav-Personident")
            logger.info("personident test: $personIdent")

            try {
                val response = httpClient.get(url) {
                    header(HttpHeaders.Authorization, "Bearer $stsToken")
                    header(HttpHeaders.NavConsumerToken, stsToken)
                    header(HttpHeaders.NavPersonident, personIdent)
                    header(HttpHeaders.NavCallId, callId)
                    pipeRequest(call)
                }
                call.pipeResponse(response)
            } catch (cause: ResponseException) {
                logger.error("Feil i kall mot aareg: ", cause)
                call.pipeResponse(cause.response)
            } catch (cause: Throwable) {
                logger.error("Feil i kall mot aareg: ", cause)
                call.respondText(status = HttpStatusCode.InternalServerError) { cause.message ?: "Intern feil" }
            }
        }
    }
}