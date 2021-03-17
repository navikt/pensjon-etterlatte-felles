package no.nav.etterlatte

import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.testsupport.TestRapid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FinnEtterlatteTest {

    @Test
    fun opprett() {
        val inspector = TestRapid()
            .apply { FinnEtterlatte(this, FinnEtterlatteMock()) }
            .apply {
                sendTestMessage(
                    JsonMessage.newMessage(
                        mapOf(
                            "@event_name" to "person_dod",
                            "@avdod_ident" to "123",
                        )
                    )
                        .toJson()
                )
            }.inspektør

        assertEquals("456", inspector.message(0).get("@etterlatt_ident").asText())
        assertEquals("etterlatt_barn_identifisert", inspector.message(0).get("@event_name").asText())
        assertEquals("456", inspector.message(0).get("@etterlatt_ident").asText())
        assertEquals("789", inspector.message(1).get("@etterlatt_ident").asText())

    }
}

class FinnEtterlatteMock : FinnEtterlatteForPerson {
    override suspend fun finnEtterlatteForPerson(forelder: String): List<String> {
        return listOf("456", "789")
    }
}