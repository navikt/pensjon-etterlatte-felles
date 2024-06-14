package no.nav.etterlatte.routes

import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentDetaljRequest
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingPortType
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingsvedtakRequest

/**
 * Endepunkter for å integrere med tilbakekrevingstjenesten fra gcp til fss
 */
fun Route.tilbakekrevingRoute(tilbakekrevingService: TilbakekrevingPortType) {
    val logger = application.log

    post("/tilbakekreving/tilbakekrevingsvedtak") {
        val request = call.receive<TilbakekrevingsvedtakRequest>()

        logger.info(
            "Videresender tilbakekrevingsvedtak med vedtakId=${request.tilbakekrevingsvedtak.vedtakId} fra proxy"
        )
        val response = tilbakekrevingService.tilbakekrevingsvedtak(request)
        call.respond(response)
    }

    get("/tilbakekreving/kravgrunnlag") {
        val request = call.receive<KravgrunnlagHentDetaljRequest>()

        logger.info(
            "Videresender henting av kravgrunnlag med kravgrunnlagId=${request.hentkravgrunnlag.kravgrunnlagId} fra proxy"
        )
        val response = tilbakekrevingService.kravgrunnlagHentDetalj(request)
        call.respond(response)
    }
}