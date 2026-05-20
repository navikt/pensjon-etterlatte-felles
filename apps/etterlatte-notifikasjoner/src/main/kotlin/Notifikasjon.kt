package no.nav.etterlatte

import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageContext
import com.github.navikt.tbd_libs.rapids_and_rivers_api.RapidsConnection
import com.github.navikt.tbd_libs.rapids_and_rivers.River
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageMetadata
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.log

class Notifikasjon(
    private val sendNotifikasjon: SendNotifikasjon,
    rapidsConnection: RapidsConnection
) : River.PacketListener {
    private val logger: Logger = LoggerFactory.getLogger(Notifikasjon::class.java)

    private val rapid = rapidsConnection

    init {
        River(rapidsConnection)
            .apply {
                validate { it.demandValue("@event_name", "soeknad_innsendt") }
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

            val soeknadId = packet["@lagret_soeknad_id"]
            if (soeknadId.textValue() != "27961") {
                logger.info("Sender notifikasjon for søknad $soeknadId")
                sendNotifikasjon.sendMessage(soeknad)
            }
            else {
                logger.warn("Hoppet over utsending av notifikasjon for søknad $soeknadId")
            }

            val journalpostId = packet["@dokarkivRetur"]["journalpostId"]
            JsonMessage
                .newMessage(
                    mapOf(
                        "@event_name" to "notifikasjon_sendt",
                        "@lagret_soeknad_id" to soeknadId,
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