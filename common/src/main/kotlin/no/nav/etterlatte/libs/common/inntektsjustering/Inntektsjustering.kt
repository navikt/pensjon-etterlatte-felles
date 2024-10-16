package no.nav.etterlatte.libs.common.inntektsjustering

import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class Inntektsjustering(
    val id: UUID,
    val fnr: String,
    val inntektsaar: Int,
    val arbeidsinntekt: Int,
    val naeringsinntekt: Int,
    val inntektFraUtland: Int,
    val AFPInntekt: Int,
    val AFPTjenesteordning: String?,
    val skalGaaAvMedAlderspensjon: String,
    val datoForAaGaaAvMedAlderspensjon: LocalDate,
    val tidspunkt: Instant
)