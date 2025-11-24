package no.nav.etterlatte.soeknad

import com.fasterxml.jackson.module.kotlin.readValue
import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import com.github.navikt.tbd_libs.rapids_and_rivers.River
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageContext
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageMetadata
import com.github.navikt.tbd_libs.rapids_and_rivers_api.RapidsConnection
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.runBlocking
import no.nav.etterlatte.SendNotifikasjon
import no.nav.etterlatte.Soeknad
import no.nav.etterlatte.mapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SoeknadNotifikasjon(
    private val sendNotifikasjon: SendNotifikasjon,
    rapidsConnection: RapidsConnection
) : River.PacketListener {
    private val logger: Logger = LoggerFactory.getLogger(SoeknadNotifikasjon::class.java)

    private val rapid = rapidsConnection

    init {
        River(rapidsConnection)
            .apply {
                precondition {
                    it.requireValue("@event_name", "soeknad_innsendt")
                }
                validate { it.requireKey("@dokarkivRetur") }
                validate { it.requireKey("@fnr_soeker") }
                validate { it.requireKey("@skjema_info") }
                validate { it.requireKey("@lagret_soeknad_id") }
            }.register(this)
    }

    override fun onPacket(
        packet: JsonMessage,
        context: MessageContext,
        metadata: MessageMetadata,
        meterRegistry: MeterRegistry
    ) {
        runBlocking {
            val soeknad = mapper.readValue<Soeknad>(packet["@skjema_info"].toString())

            sendNotifikasjon.sendSMSVarselTilBruker(
                foedselsnummer = soeknad.innsender.foedselsnummer.value,
                varselTekst = when (soeknad.type) {
                    Soeknad.Type.BARNEPENSJON -> "Vi har mottatt søknaden din om barnepensjon"
                    Soeknad.Type.OMSTILLINGSSTOENAD -> "Vi har mottatt søknaden din om omstillingsstønad"
                }
            )

            val journalpostId = packet["@dokarkivRetur"]["journalpostId"]
            JsonMessage.Companion
                .newMessage(
                    mapOf(
                        "@event_name" to "notifikasjon_sendt",
                        "@lagret_soeknad_id" to packet["@lagret_soeknad_id"],
                        "@journalpostId" to journalpostId,
                        "@notifikasjon" to "Notifikasjon sendt"
                    )
                ).apply {
                    try {
                        rapid.publish("SendNotifikasjon " + journalpostId.textValue(), toJson())
                    } catch (err: Exception) {
                        logger.error("Uhaandtert feilsituasjon. Ingen notifikasjon opprettet: ", err)
                    }
                }
            logger.info("Notifikasjon til bruker opprettet")
        }
    }
}