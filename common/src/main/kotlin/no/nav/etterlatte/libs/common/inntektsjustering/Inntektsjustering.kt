package no.nav.etterlatte.libs.common.inntektsjustering

import java.time.Instant
import java.util.UUID

data class Inntektsjustering(
    val id: UUID,
    val inntektsaar: Int,
    val arbeidsinntekt: Int,
    val naeringsinntekt: Int,
    val arbeidsinntektUtland: Int,
    val naeringsinntektUtland: Int,
    val tidspunkt: Instant,
)