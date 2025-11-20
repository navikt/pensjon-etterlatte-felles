package no.nav.etterlatte

import no.nav.tms.varsel.action.EksternKanal
import no.nav.tms.varsel.action.Produsent
import no.nav.tms.varsel.action.Sensitivitet
import no.nav.tms.varsel.action.Tekst
import no.nav.tms.varsel.action.Varseltype
import no.nav.tms.varsel.builder.VarselActionBuilder
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit

class SendNotifikasjon(
    val env: Map<String, String>,
    private val producer: Producer<String, String> = KafkaProducer(KafkaConfig().getProducerConfig(env))
) {
    private val logger: Logger = LoggerFactory.getLogger(SendNotifikasjon::class.java)
    private val brukernotifikasjontopic = env["BRUKERNOTIFIKASJON_BESKJED_TOPIC"]!!

    fun sendMessage(soeknad: Soeknad) {
        val innsender = soeknad.innsender.foedselsnummer

        sendVarselSMS(innsender, soeknad.type)
    }

    internal fun sendVarselSMS(foedselsnummer: Foedselsnummer, soeknadType: Soeknad.Type) {

        val oppgaveId = UUID.randomUUID().toString()

        val varslingTekst = when (soeknadType) {
            Soeknad.Type.BARNEPENSJON -> "Vi har mottatt søknaden din om barnepensjon"
            Soeknad.Type.OMSTILLINGSSTOENAD -> "Vi har mottatt søknaden din om omstillingsstønad"
        }

        val varsel =  VarselActionBuilder.opprett {
            type = Varseltype.Beskjed
            varselId = UUID.randomUUID().toString()
            sensitivitet = Sensitivitet.High
            ident = foedselsnummer.value
            tekst = Tekst(
                spraakkode = "nb",
                tekst = varslingTekst,
                default = true
            )
            aktivFremTil = ZonedDateTime.now().plusDays(7)
            eksternVarsling {
                preferertKanal = EksternKanal.SMS
                smsVarslingstekst = varslingTekst
            }
            produsent = Produsent(
                cluster = env["NAIS_CLUSTER_NAME"]!!,
                appnavn = env["NAIS_APP_NAME"]!!,
                namespace = env["NAIS_NAMESPACE"]!!
            )
        }
        try {
            producer.send(ProducerRecord(brukernotifikasjontopic, oppgaveId, varsel)).get(10, TimeUnit.SECONDS)
        } catch (e: Exception) {
            logger.error(
                "Beskjed til $brukernotifikasjontopic (Ditt NAV) for søknad med id ${oppgaveId} feilet.",
                e
            )
        }
    }
}