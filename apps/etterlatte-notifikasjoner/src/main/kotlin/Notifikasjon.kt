package no.nav.etterlatte

import kotlinx.coroutines.runBlocking
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageContext
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.River
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class Notifikasjon(private val sendNotifikasjon: SendNotifikasjon, rapidsConnection: RapidsConnection) :
    River.PacketListener {

    private val logger: Logger = LoggerFactory.getLogger("no.pensjon.etterlatte")

    private val rapid = rapidsConnection
    init {
        sendNotifikasjon.startuptask()
        River(rapidsConnection).apply {
            validate { it.demandValue("@event_name", "soeknad_innsendt") }
            validate { it.requireKey("@dokarkivRetur") }
            validate { it.requireKey("@fnr_soeker") }
            validate { it.requireKey("@lagret_soeknad_id") }
        }.register(this)

    }

    override fun onPacket(packet: JsonMessage, context: MessageContext) {

        runBlocking {

            sendNotifikasjon.sendMessage(packet["@fnr_soeker"].textValue())

            val journalpostId = packet["@dokarkivRetur"]["journalpostId"]
            JsonMessage.newMessage(mapOf(
                "@event_name" to "notifikasjon_sendt",
                "@lagret_soeknad_id" to packet["@lagret_soeknad_id"],
                "@journalpostId" to journalpostId ,
                "@notifikasjon" to "Notifikasjon sendt",
            )).apply {
                rapid.publish("SendNotifikasjon " + journalpostId.textValue(), toJson())
            }
            logger.info("Notifikasjon til bruker opprettet")
        }
    }
}





