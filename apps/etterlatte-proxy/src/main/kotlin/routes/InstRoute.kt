package no.nav.etterlatte.routes

import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import no.nav.etterlatte.Config
import no.nav.etterlatte.StsClient
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import no.nav.etterlatte.NavCallId
import no.nav.etterlatte.NavConsumerId
import no.nav.etterlatte.httpClient
import no.nav.etterlatte.pipeRequest
import no.nav.etterlatte.pipeResponse
import org.slf4j.LoggerFactory
import java.util.*

fun Route.institusjonsoppholdRoute(config: Config, stsClient: StsClient) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    val inst2RouteSuffix = "api/v1/person/institusjonsopphold/"
    route("/inst2/{oppholdId}") {
        val httpClient = httpClient()
        val inst2Url = config.institusjonsoppholdUrl

        get {
            val stsToken = stsClient.getToken()
            val callId = call.request.header(HttpHeaders.NavCallId) ?: UUID.randomUUID().toString()
            val oppholdId =
                call.parameters["oppholdId"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Mangler oppholdsid")
            try {
                val response = httpClient.post(inst2Url.plus(inst2RouteSuffix.plus(oppholdId))) {
                    bearerAuth(stsToken.accessToken)
                    header(HttpHeaders.NavConsumerId, "etterlatte-proxy")
                    header(HttpHeaders.NavCallId, callId)
                    pipeRequest(call)
                }
                call.pipeResponse(response)

            } catch (cause: ResponseException) {
                logger.error("Feil i kall mot institusjonsopphold: ", cause)
                call.pipeResponse(cause.response)
            } catch (cause: Throwable) {
                logger.error("Feil i kall mot institusjonsopphold: ", cause)
                call.respondText(status = HttpStatusCode.InternalServerError) { cause.message ?: "Intern feil" }
            }
        }
    }
}