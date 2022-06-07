package no.nav.etterlatte.routes

import io.ktor.application.call
import io.ktor.client.features.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import no.nav.etterlatte.Config
import no.nav.etterlatte.NavCallId
import no.nav.etterlatte.NavConsumerId
import no.nav.etterlatte.StsClient
import no.nav.etterlatte.httpClient
import no.nav.etterlatte.pipeRequest
import no.nav.etterlatte.pipeResponse
import org.slf4j.LoggerFactory

fun Route.inntektskomponenten(config: Config, stsClient: StsClient) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    route("/inntektskomponenten") {
        val httpClient = httpClient()
        val dokUrl = config.inntektskomponenten.url

        post {
            val stsToken = stsClient.getToken()

            try {
                val response = httpClient.post<HttpResponse>(dokUrl) {
                    header(HttpHeaders.Authorization, "Bearer $stsToken")
                    header(HttpHeaders.NavConsumerId, "barnepensjon")
                    header(HttpHeaders.NavCallId, HttpHeaders.NavCallId)
                    pipeRequest(call)
                }
                call.pipeResponse(response)

            } catch (cause: ResponseException) {
                logger.error("Feil i kall mot inntektskomponenten: ", cause)
                call.pipeResponse(cause.response)
            } catch (cause: Throwable) {
                logger.error("Feil i kall mot inntektskomponenten: ", cause)
                call.respondText(status = HttpStatusCode.InternalServerError) { cause.message ?: "Intern feil" }
            }
        }
    }
}