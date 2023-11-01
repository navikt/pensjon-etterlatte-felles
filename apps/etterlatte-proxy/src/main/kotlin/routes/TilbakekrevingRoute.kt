package no.nav.etterlatte.routes

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingPortType
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingsvedtakRequest
import javax.xml.datatype.XMLGregorianCalendar

/**
 * Endepunkter for å integrere med tilbakekrevingstjenesten fra gcp til fss
 */
fun Route.tilbakekrevingRoute(tilbakekrevingService: TilbakekrevingPortType) {
    val logger = application.log

    post("/tilbakekreving/tilbakekrevingsvedtak") {
        val request = call.receiveText()
        logger.info(request)

        val vedtakRequest: TilbakekrevingsvedtakRequest = xmlMapper.readValue(request)
        logger.info("Videresender tilbakekrevingsvedtak ${vedtakRequest.tilbakekrevingsvedtak.vedtakId} til on-prem")

        logger.info(xmlMapper.writeValueAsString(vedtakRequest))
        val response = tilbakekrevingService.tilbakekrevingsvedtak(vedtakRequest)
        logger.info(xmlMapper.writeValueAsString(response))

        call.respond(response)
    }
}

val xmlMapper = XmlMapper(JacksonXmlModule().apply { setDefaultUseWrapper(false) }).apply {
    registerModule(KotlinModule.Builder().build())
    registerModule(JavaTimeModule())
    registerModule(CustomXMLGregorianCalendarModule())
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}

private class CustomXMLGregorianCalendarModule : SimpleModule() {
    init {
        addSerializer(XMLGregorianCalendar::class.java, CustomXMLGregorianCalendarSerializer())
    }

    private class CustomXMLGregorianCalendarSerializer : JsonSerializer<XMLGregorianCalendar>() {
        override fun serialize(value: XMLGregorianCalendar?, gen: JsonGenerator, serializers: SerializerProvider) {
            if (value != null) {
                gen.writeString(value.toGregorianCalendar().toZonedDateTime().toLocalDate().toString())
            }
        }
    }
}