package no.nav.etterlatte.libs.common.innsendtsoeknad.utvidetomstillingsstoenad

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import no.nav.etterlatte.libs.common.innsendtsoeknad.BankkontoType
import no.nav.etterlatte.libs.common.innsendtsoeknad.ForventetInntektIAar
import no.nav.etterlatte.libs.common.innsendtsoeknad.Spraak
import no.nav.etterlatte.libs.common.innsendtsoeknad.StudieformType
import no.nav.etterlatte.libs.common.innsendtsoeknad.UtbetalingsInformasjon
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.BetingetOpplysning
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.DatoSvar
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.EnumSvar
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.FritekstSvar
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.GjenlevendeUtvidetOMS
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.ImageTag
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Innsender
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.InnsendtSoeknad
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.JaNeiVetIkke
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Opplysning
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Samboer
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.SoeknadType
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class UtvidetOmstillingsstoenad(
    override val imageTag: ImageTag,
    override val spraak: Spraak,
    override val innsender: Innsender,
    override val harSamtykket: Opplysning<Boolean>,
    override val soeker: GjenlevendeUtvidetOMS,
    val situasjonenDinIDag: SituasjonenDinIDag,
    val utdanningOgTiltak: UtdanningOgTiltak,
    val inntekt: ForventetInntektIAar,
    override val utbetalingsInformasjon: BetingetOpplysning<EnumSvar<BankkontoType>, UtbetalingsInformasjon>? = null,
    override val mottattDato: LocalDateTime = LocalDateTime.now(),
) : InnsendtSoeknad {
    override val versjon = "1"
    override val type: SoeknadType = SoeknadType.UTVIDET_OMSTILLINGSSTOENAD
}

data class SituasjonenDinIDag(
    val nySivilstand: BetingetOpplysning<EnumSvar<NySivilstandValg>, Samboer?>,
)

enum class NySivilstandValg { NY_SAMBOER, GIFTET_PAA_NYTT, INGEN_AV_DELENE }

data class UtdanningOgTiltak(
    val aktivitet: Opplysning<EnumSvar<UtdanningOgTiltakValg>>,
    val utdanning: UtdanningUtvidetOMS? = null,
    val arbeidsrettetTiltak: ArbeidsrettetTiltak? = null,
    val aktivitetsplan: Opplysning<EnumSvar<JaNeiVetIkke>>? = null,
)

enum class UtdanningOgTiltakValg { UTDANNING, ARBEIDSRETTET_TILTAK, INGEN }

data class UtdanningUtvidetOMS(
    val studiested: Opplysning<FritekstSvar>,
    val studie: Opplysning<FritekstSvar>,
    val startDato: Opplysning<DatoSvar>,
    val sluttDato: Opplysning<DatoSvar>,
    val studieform: Opplysning<EnumSvar<StudieformType>>,
    val studieprosent: Opplysning<FritekstSvar>? = null,
)

data class ArbeidsrettetTiltak(
    val tiltak: Opplysning<FritekstSvar>,
    val omfang: Opplysning<EnumSvar<TiltakOmfangType>>,
)

enum class TiltakOmfangType { HELTID, DELTID }
