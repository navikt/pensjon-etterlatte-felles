package no.nav.etterlatte.libs.common.omsmeldinnendring

import no.nav.etterlatte.libs.common.person.Foedselsnummer
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

data class OmsMeldtInnEndringRequest(
    val endring: OmsEndring,
    val beskrivelse: String,
)

data class OmsMeldtInnEndring(
    val id: UUID = UUID.randomUUID(),
    val fnr: Foedselsnummer,
    val endring: OmsEndring,
    val beskrivelse: String,
    val tidspunkt: Instant = Instant.now().truncatedTo(ChronoUnit.SECONDS),
)

enum class OmsEndring {
    INNTEKT,
    AKTIVITET_OG_INNTEKT,
    ANNET,
}

enum class OmsMeldtInnEndringStatus {
    LAGRET,
    SENDT,
    FERDIGSTILT,
}