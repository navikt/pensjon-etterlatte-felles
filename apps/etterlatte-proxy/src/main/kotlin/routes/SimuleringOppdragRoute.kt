package no.nav.etterlatte.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SimulerBeregningFeilUnderBehandling
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SimulerFpService
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningRequest

/**
 * Endepunkter for å integrere med simuleringstjenesten i OS fra gcp til fss
 */
fun Route.simuleringOppdragRoute(simulerFpService: SimulerFpService) {
    val logger = application.log

    post("/simuleringoppdrag/simulerberegning") {
        val request = call.receive<SimulerBeregningRequest>()

        logger.info("Videresender simuleringsberegning for fagsystemId=${request.request.oppdrag.fagsystemId} fra proxy")
        try {
            val response = simulerFpService.simulerBeregning(request)
            call.respond(response)
        } catch (e: SimulerBeregningFeilUnderBehandling) {
            logger.error("Feil ved kall til simuleringstjeneste i Oppdrag", e)
            call.respond(HttpStatusCode.InternalServerError, e.faultInfo)
        }
    }
}