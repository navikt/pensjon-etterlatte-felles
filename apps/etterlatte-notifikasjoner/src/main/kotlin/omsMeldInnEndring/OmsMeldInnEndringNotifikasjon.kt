package no.nav.etterlatte.omsMeldInnEndring

import com.fasterxml.jackson.module.kotlin.readValue
import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import com.github.navikt.tbd_libs.rapids_and_rivers.River
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageContext
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageMetadata
import com.github.navikt.tbd_libs.rapids_and_rivers_api.RapidsConnection
import io.micrometer.core.instrument.MeterRegistry
import no.nav.etterlatte.SendNotifikasjon
import no.nav.etterlatte.libs.common.omsmeldinnendring.OmsMeldtInnEndring
import no.nav.etterlatte.mapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OmsMeldInnEndringNotifikasjon(
    private val sendNotifikasjon: SendNotifikasjon,
    rapidsConnection: RapidsConnection
) : River.PacketListener {
    private val logger: Logger = LoggerFactory.getLogger(OmsMeldInnEndringNotifikasjon::class.java)

    private val rapid = rapidsConnection

    init {
        River(rapidsConnection).apply {
            validate {
                precondition {
                    it.requireValue("@event_name", "oms_meldt_inn_endring")
                }
                validate { it.requireKey("@oms_meldt_inn_endring_innhold") }
            }
        }.register(this)
    }

    override fun onPacket(
        packet: JsonMessage,
        context: MessageContext,
        metadata: MessageMetadata,
        meterRegistry: MeterRegistry
    ) {
        val meldtInnEndring = mapper.readValue<OmsMeldtInnEndring>(packet["@oms_meldt_inn_endring_innhold"].toString())

        sendNotifikasjon.sendSMSVarselTilBruker(
            foedselsnummer = meldtInnEndring.fnr.value,
            varselTekst = "Vi har mottatt endringen din"
        )

        logger.info("Notifikasjon til bruker opprettet")
    }
}