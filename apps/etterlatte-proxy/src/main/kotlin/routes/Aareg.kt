package no.nav.etterlatte.routes

import io.ktor.application.call
import io.ktor.client.features.ResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import no.nav.etterlatte.Config
import no.nav.etterlatte.NavCallId
import no.nav.etterlatte.NavConsumerToken
import no.nav.etterlatte.NavPersonident
import no.nav.etterlatte.StsClient
import no.nav.etterlatte.httpClient
import no.nav.etterlatte.pipeRequest
import no.nav.etterlatte.pipeResponse
import org.slf4j.LoggerFactory
import java.util.*

fun Route.aareg(config: Config, stsClient: StsClient) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    route("/aareg/arbeidstaker/arbeidsforhold") {
        val httpClient = httpClient()
        val url = config.aareg.url + "/arbeidstaker/arbeidsforhold"

        post {
            val stsToken = stsClient.getToken()
            val callId = call.request.header(HttpHeaders.NavCallId) ?: UUID.randomUUID().toString()
            val personIdent = call.request.header("Nav-Personident")
            print("personident test: $personIdent")
            try {
                val response = httpClient.post<HttpResponse>(url) {
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

