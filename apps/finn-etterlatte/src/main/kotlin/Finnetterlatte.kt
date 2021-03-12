package no.nav.etterlatte

import kotlinx.coroutines.runBlocking
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageProblems
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.River


internal class FinnEtterlatte(rapidsConnection: RapidsConnection, private val pdl: FinnEtterlatteForPerson) :
    River.PacketListener {

    init {
        River(rapidsConnection).apply {
            validate { it.demandValue("@event_name", "person_dod") }
            validate { it.requireKey("@ident") }
        }.register(this)
    }

    override fun onPacket(packet: JsonMessage, context: RapidsConnection.MessageContext) {
        println(packet["@ident"].asText())


        runBlocking {
            pdl.finnEtterlatteForPerson(packet["@ident"].asText()).forEach {
                context.send(JsonMessage("{}", MessageProblems("{}")).apply {
                    set("@ident", it)
                    set("@Ident_avdod", packet["@ident"])
                    set("@event_name", "etterlatt_barn_identifisert")
                }.toJson())
            }
        }
        // nested objects can be chained using "."
        // println(packet["nested.key"].asText())
    }
}


internal class Monitor(rapidsConnection: RapidsConnection) : River.PacketListener {

    init {
        River(rapidsConnection).apply {
            validate { it.demandValue("@event_name", "etterlatt_barn_identifisert") }
        }.register(this)
    }

    override fun onError(problems: MessageProblems, context: RapidsConnection.MessageContext) {
    }

    override fun onPacket(packet: JsonMessage, context: RapidsConnection.MessageContext) {
        println(packet.toJson())
    }
}

interface FinnEtterlatteForPerson {
    suspend fun finnEtterlatteForPerson(forelder: String): List<String>
}
