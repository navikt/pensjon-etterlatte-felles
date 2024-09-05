package no.nav.etterlatte.libs.common.innsendtsoeknad.omstillingsstoenad

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import no.nav.etterlatte.libs.common.innsendtsoeknad.BankkontoType
import no.nav.etterlatte.libs.common.innsendtsoeknad.Spraak
import no.nav.etterlatte.libs.common.innsendtsoeknad.UtbetalingsInformasjon
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Avdoed
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Barn
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.BetingetOpplysning
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.EnumSvar
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.GjenlevendeOMS
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.ImageTag
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Innsender
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.InnsendtSoeknad
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Opplysning
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.SoeknadType
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Omstillingsstoenad(
    override val imageTag: ImageTag,
    override val spraak: Spraak,
    override val innsender: Innsender,
    override val harSamtykket: Opplysning<Boolean>,
    override val utbetalingsInformasjon: BetingetOpplysning<EnumSvar<BankkontoType>, UtbetalingsInformasjon>?,
    override val soeker: GjenlevendeOMS,
    val avdoed: Avdoed,
    val barn: List<Barn>,
) : InnsendtSoeknad {
    override val versjon = "1"
    override val type: SoeknadType = SoeknadType.OMSTILLINGSSTOENAD
    override val mottattDato: LocalDateTime = LocalDateTime.now()
}
