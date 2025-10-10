package no.nav.etterlatte.libs.common.omsmeldinnendring

import no.nav.etterlatte.libs.common.person.Foedselsnummer
import java.time.Instant
import java.time.LocalDate
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
    val forventetInntektTilNesteAar: ForventetInntektTilNesteAar?,
    val beskrivelse: String,
    val tidspunkt: Instant = Instant.now().truncatedTo(ChronoUnit.SECONDS),
)

data class ForventetInntektTilNesteAar(
    val inntektsaar: Int,
    val arbeidsinntekt: Int,
    val naeringsinntekt: Int,
    val inntektFraUtland: Int,
    val afpInntekt: Int?,
    val afpTjenesteordning: String?,
    val skalGaaAvMedAlderspensjon: String?,
    val datoForAaGaaAvMedAlderspensjon: LocalDate?
)

enum class OmsEndring {
    INNTEKT,
    AKTIVITET_OG_INNTEKT,
    SVAR_PAA_ETTEROPPGJOER,
    FORVENTET_INNTEKT_TIL_NESTE_AAR,
    ANNET,
}

enum class OmsMeldtInnEndringStatus {
    LAGRET,
    SENDT,
    FERDIGSTILT,
}