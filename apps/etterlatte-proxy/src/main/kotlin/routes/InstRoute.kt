package no.nav.etterlatte.routes

import com.typesafe.config.ConfigFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import no.nav.etterlatte.Config
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.coroutines.runBlocking
import no.nav.etterlatte.NavCallId
import no.nav.etterlatte.NavConsumerId
import no.nav.etterlatte.getInstitusonsOppholdHttpklient
import no.nav.etterlatte.pipeResponse
import org.slf4j.LoggerFactory
import java.util.*

fun Route.institusjonsoppholdRoute(config: Config) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    val inst2RouteSuffix = "api/v1/person/institusjonsopphold/"
    val defaultConfig = ConfigFactory.load()
    val httpKlient = getInstitusonsOppholdHttpklient(defaultConfig).also {
        if(environment?.developmentMode == false) {
            runBlocking {
                it.get("api/ping") {
                    header(HttpHeaders.NavConsumerId, "etterlatte-proxy")
                }.let {
                    if (it.status == HttpStatusCode.OK) {
                        logger.info("Successfully pinged inst2-core")
                    } else {
                        logger.error("Couldnt not ping inst2-core, status: ${it.status}")
                    }
                }
            }
        }
    }
    route("/inst2/{oppholdId}") {
        val inst2Url = config.institusjonsoppholdUrl

        get {
            val callId = call.request.header(HttpHeaders.NavCallId) ?: UUID.randomUUID().toString()
            val oppholdId =
                call.parameters["oppholdId"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Mangler oppholdsid")
            try {
                val response = httpKlient.post(inst2Url.plus(inst2RouteSuffix.plus(oppholdId))) {
                    header(HttpHeaders.NavConsumerId, "etterlatte-proxy")
                    header(HttpHeaders.NavCallId, callId)
                }
                call.pipeResponse(response)

            } catch (cause: ResponseException) {
                logger.error("Feil i kall mot institusjonsopphold: ", cause)
                call.respond(HttpStatusCode.InternalServerError, cause.response)
            } catch (cause: Throwable) {
                logger.error("Feil i kall mot institusjonsopphold: ", cause)
                call.respondText(status = HttpStatusCode.InternalServerError) { cause.message ?: "Intern feil" }
            }
        }
    }
}