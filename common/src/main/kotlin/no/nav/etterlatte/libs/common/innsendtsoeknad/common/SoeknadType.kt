package no.nav.etterlatte.libs.common.innsendtsoeknad.common

enum class SoeknadType(
    val behandlingsnummer: Behandlingsnummer,
) {
    BARNEPENSJON(Behandlingsnummer.BARNEPENSJON),
    OMSTILLINGSSTOENAD(Behandlingsnummer.OMSTILLINGSSTOENAD),
}

enum class Behandlingsnummer(
    val verdi: String,
) {
    BARNEPENSJON("B359"),
    OMSTILLINGSSTOENAD("B373"),
}
