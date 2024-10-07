import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.mockk
import no.nav.brukernotifikasjon.schemas.input.BeskjedInput
import no.nav.brukernotifikasjon.schemas.input.NokkelInput
import no.nav.etterlatte.Notifikasjon
import no.nav.etterlatte.SendNotifikasjon
import no.nav.etterlatte.Soeknad
import no.nav.etterlatte.mapper
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.testsupport.TestRapid
import org.apache.kafka.clients.producer.MockProducer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class NotifikasjonTest {
    private val mockKafkaProducer =
        MockProducer<NokkelInput, BeskjedInput>(true, mockk(relaxed = true), mockk(relaxed = true))

    private val sendMelding =
        SendNotifikasjon(
            mapOf(
                "BRUKERNOTIFIKASJON_BESKJED_TOPIC" to "test_topic",
                "BRUKERNOTIFIKASJON_KAFKA_GROUP_ID" to "bah",
                "NAIS_NAMESPACE" to "etterlatte",
                "NAIS_NAME" to "etterlatte-notifikasjoner"
            ),
            mockKafkaProducer
        )

    @BeforeEach
    fun before() {
        mockKafkaProducer.clear()
    }

    @Test
    fun `Skal opprette notifikasjon til innsender ved innsending av barnepensjon`() {
        val json = this::class.java.getResource("barnepensjon.json")!!.readText()
        val soeknad: Soeknad = mapper.readValue(json)

        val inspector =
            TestRapid()
                .apply {
                    Notifikasjon(
                        sendMelding,
                        this
                    )
                }.apply {
                    sendTestMessage(
                        JsonMessage
                            .newMessage(
                                mapOf(
                                    "@event_name" to "soeknad_innsendt",
                                    "@dokarkivRetur" to "123456",
                                    "@fnr_soeker" to "07106123912",
                                    "@skjema_info" to mapper.readTree(json),
                                    "@lagret_soeknad_id" to "4",
                                    "@dokarkivRetur" to (mapOf("journalpostId" to "5"))
                                )
                            ).toJson()
                    )
                }.inspektør

        assertEquals("notifikasjon_sendt", inspector.message(0).get("@event_name").asText())
        assertEquals("Notifikasjon sendt", inspector.message(0).get("@notifikasjon").asText())
        assertEquals("5", inspector.message(0).get("@journalpostId").asText())
        assertEquals("4", inspector.message(0).get("@lagret_soeknad_id").asText())
        assertEquals("SendNotifikasjon 5", inspector.key(0))
        assertEquals(mockKafkaProducer.history().size, 1)
        assertEquals(mockKafkaProducer.history()[0].value().tekst, "Vi har mottatt søknaden din om barnepensjon")
        assertEquals(
            soeknad.innsender.foedselsnummer.value,
            mockKafkaProducer.history()[0].key().fodselsnummer
        )
    }

    @Test
    fun `Skal opprette notifikasjon til innsender ved innsending av omstillingsstoenad`() {
        val json = this::class.java.getResource("omstillingsstoenad.json")!!.readText()
        val soeknad: Soeknad = mapper.readValue(json)

        val inspector =
            TestRapid()
                .apply {
                    Notifikasjon(
                        sendMelding,
                        this
                    )
                }.apply {
                    sendTestMessage(
                        JsonMessage
                            .newMessage(
                                mapOf(
                                    "@event_name" to "soeknad_innsendt",
                                    "@dokarkivRetur" to "123456",
                                    "@fnr_soeker" to "07106123912",
                                    "@skjema_info" to mapper.readTree(json),
                                    "@lagret_soeknad_id" to "4",
                                    "@dokarkivRetur" to (mapOf("journalpostId" to "5"))
                                )
                            ).toJson()
                    )
                }.inspektør

        assertEquals("notifikasjon_sendt", inspector.message(0).get("@event_name").asText())
        assertEquals("Notifikasjon sendt", inspector.message(0).get("@notifikasjon").asText())
        assertEquals("5", inspector.message(0).get("@journalpostId").asText())
        assertEquals("4", inspector.message(0).get("@lagret_soeknad_id").asText())
        assertEquals("SendNotifikasjon 5", inspector.key(0))
        assertEquals(mockKafkaProducer.history().size, 1)
        assertEquals(
            mockKafkaProducer.history()[0].value().tekst,
            "Vi har mottatt søknaden din om omstillingsstønad"
        )
        assertEquals(
            soeknad.innsender.foedselsnummer.value,
            mockKafkaProducer.history()[0].key().fodselsnummer
        )
    }
}