package no.nav.etterlatte.routes

import io.ktor.application.call
import io.ktor.client.features.ResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import no.nav.etterlatte.Config
import no.nav.etterlatte.StsClient
import no.nav.etterlatte.httpClient
import no.nav.etterlatte.pipeRequest
import no.nav.etterlatte.pipeResponse
import org.slf4j.LoggerFactory


fun Route.regoppslag(config: Config, stsClient: StsClient) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    route("/regoppslag") {
        val httpClient = httpClient()
        val regoppslagUrl = config.regoppslag.url

        get("{ident}") {
            val stsToken = stsClient.getToken()

            try {
                val id = call.parameters["ident"]!!

                val response = httpClient.post<HttpResponse>(regoppslagUrl + "/postadresse") {
                    header(HttpHeaders.Authorization, "Bearer $stsToken")
                    header("Nav_Callid", "barnepensjon")
                    body = AdresseRequest("11057523044")
                    pipeRequest(call)
                }
                call.pipeResponse(response)
            } catch (cause: ResponseException) {
                logger.error("Feil i kall mot Regoppslag: ", cause)
                call.pipeResponse(cause.response)
            } catch (cause: Throwable) {
                logger.error("Feil i kall mot Regoppslag: ", cause)
                call.respondText(status = HttpStatusCode.InternalServerError) { cause.message ?: "Intern feil" }
            }
        }
    }
}

data class AdresseRequest(
    val ident: String,
    val tema: String = "PEN"
)