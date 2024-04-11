package no.nav.etterlatte.routes

import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SimulerFpService
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningRequest

/**
 * Endepunkter for å integrere med simuleringstjenesten i OS fra gcp til fss
 */
fun Route.simuleringOppdragRoute(simulerFpService: SimulerFpService) {
    val logger = application.log

    post("/simuleringoppdrag/simulerberegning") {
        val request = call.receive<SimulerBeregningRequest>()

        logger.info("Videresender simuleringsberegning for vedtakId=${request.request.oppdrag.oppdragslinje.first().vedtakId} fra proxy")
        val response = simulerFpService.simulerBeregning(request)
        call.respond(response)
    }
}