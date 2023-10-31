package no.nav.etterlatte.routes

import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Marshaller
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingPortType
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingsvedtakRequest
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingsvedtakResponse
import java.io.StringWriter
import javax.xml.stream.XMLInputFactory
import javax.xml.transform.stream.StreamSource

/**
 * Endepunkter for å integrere med tilbakekrevingstjenesten fra gcp til fss
 */
fun Route.tilbakekrevingRoute(tilbakekrevingService: TilbakekrevingPortType) {
    val logger = application.log

    post("/tilbakekreving/tilbakekrevingsvedtak") {
        val request = call.receiveText()
        logger.info(request)

        val vedtakRequest = toTilbakekrevingsvedtakRequest(request)

        logger.info("Videresender tilbakekrevingsvedtak ${vedtakRequest.tilbakekrevingsvedtak.vedtakId} til on-prem")
        val response = tilbakekrevingService.tilbakekrevingsvedtak(vedtakRequest)
        logger.info(toXml(response))

        call.respond(response)
    }


}

val jaxbContext = JAXBContext.newInstance(TilbakekrevingsvedtakRequest::class.java)
val jaxbContextResponse = JAXBContext.newInstance(TilbakekrevingsvedtakResponse::class.java)
val xmlInputFactory = XMLInputFactory.newInstance()

fun toXml(response: TilbakekrevingsvedtakResponse): String {
    val marshaller = jaxbContextResponse.createMarshaller()
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)

    val stringWriter = StringWriter()
    stringWriter.use {
        marshaller.marshal(response, stringWriter)
    }

    return stringWriter.toString()
}
fun toTilbakekrevingsvedtakRequest(xml: String): TilbakekrevingsvedtakRequest {
    val request =
        jaxbContext.createUnmarshaller().unmarshal(
            xmlInputFactory.createXMLStreamReader(StreamSource(xml)),
            TilbakekrevingsvedtakRequest::class.java,
        )
    return request.value
}