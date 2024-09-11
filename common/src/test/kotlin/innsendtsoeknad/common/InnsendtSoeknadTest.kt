package innsendtsoeknad.common

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import no.nav.etterlatte.libs.common.innsendtsoeknad.barnepensjon.Barnepensjon
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.InnsendtSoeknad
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.SoeknadRequest
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.SoeknadType
import no.nav.etterlatte.libs.common.innsendtsoeknad.omstillingsstoenad.Omstillingsstoenad
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Suppress("ktlint:standard:max-line-length")
internal class InnsendtSoeknadTest {
    private val mapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())

    @Test
    fun `Deserialisering fungerer som forventet`() {
        val json = """{"soeknader":[{"imageTag":"d79803f3acb657cf657ee46af1db293a665eb0d2","spraak":"nb","type":"OMSTILLINGSSTOENAD","harSamtykket":{"spoersmaal":"Jeg vil svare så godt jeg kan på spørsmålene i søknaden.","svar":true},"innsender":{"type":"INNSENDER","fornavn":{"spoersmaal":"Fornavn","svar":"KALD"},"etternavn":{"spoersmaal":"Etternavn","svar":"FOLK"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"24876696580"}},"utbetalingsInformasjon":{"spoersmaal":"Ønsker du å motta utbetalingen på norsk eller utenlandsk bankkonto?","svar":{"verdi":"NORSK","innhold":"Norsk"},"opplysning":{"kontonummer":{"spoersmaal":"Oppgi norsk kontonummer for utbetaling","svar":{"innhold":"1241.24.12412"}}}},"soeker":{"type":"GJENLEVENDE_OMS","fornavn":{"spoersmaal":"Fornavn","svar":"KALD"},"etternavn":{"spoersmaal":"Etternavn","svar":"FOLK"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"24876696580"},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":"Norge"},"sivilstatus":{"spoersmaal":"Sivilstatus","svar":"Gift"},"adresse":{"spoersmaal":"Bostedsadresse","svar":"Nedre Ovrå 28, 6212 Liabygda"},"kontaktinfo":{"telefonnummer":{"spoersmaal":"Telefonnummer","svar":{"innhold":"+4799999999"}}},"oppholdUtland":{"spoersmaal":"Er du bosatt i Norge?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"oppholderSegIUtlandet":{"spoersmaal":"Har du bodd eller oppholdt deg i utlandet de siste 12 månedene?","svar":{"verdi":"NEI","innhold":"Nei"}}}},"nySivilstatus":{"spoersmaal":"Sivilstanden din i dag","svar":{"verdi":"ENSLIG","innhold":"Enslig"}},"arbeidOgUtdanning":{"dinSituasjon":{"spoersmaal":"Hva er situasjonen din nå?","svar":[{"verdi":"ARBEIDSTAKER","innhold":"Jeg er arbeidstaker og/eller lønnsmottaker som frilanser"}]},"arbeidsforhold":{"spoersmaal":"Om arbeidsforholdet ditt","svar":[{"arbeidsgiver":{"spoersmaal":"Navn på arbeidssted","svar":{"innhold":"NAV"}},"arbeidsmengde":{"spoersmaal":"Fyll ut stillingsprosenten din","svar":{"innhold":"100% Prosent"}},"ansettelsesforhold":{"spoersmaal":"Hva slags type ansettelsesforhold har du?","svar":{"verdi":"FAST","innhold":"Fast ansatt"}},"endretArbeidssituasjon":{"spoersmaal":"Forventer du endringer i arbeidsforholdet ditt fremover i tid?","svar":{"verdi":"NEI","innhold":"Nei"}}},{"arbeidsgiver":{"spoersmaal":"Navn på arbeidssted","svar":{"innhold":"EH"}},"arbeidsmengde":{"spoersmaal":"Fyll ut stillingsprosenten din","svar":{"innhold":"30 Prosent"}},"ansettelsesforhold":{"spoersmaal":"Hva slags type ansettelsesforhold har du?","svar":{"verdi":"FAST","innhold":"Fast ansatt"}},"endretArbeidssituasjon":{"spoersmaal":"Forventer du endringer i arbeidsforholdet ditt fremover i tid?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"spoersmaal":"Gi en kort beskrivelse av endringene","svar":{"innhold":"Hmmm"}}}}]}},"fullfoertUtdanning":{"spoersmaal":"Hva er din høyeste fullførte utdanning?","svar":[{"verdi":"UNIVERSITET_OVER_4_AAR","innhold":"Universitet eller høyskole mer enn 4 år"},{"verdi":"FAGBREV","innhold":"Fagbrev"},{"verdi":"ANNEN","innhold":"Annen utdanning"}]},"inntektOgPensjon":{"loennsinntekt":{"spoersmaal":"Arbeidsinntekt","svar":{"norgeEllerUtland":{"spoersmaal":"Hvor har du arbeidsinntekt fra?","svar":[{"verdi":"NORGE","innhold":"Norge"},{"verdi":"UTLAND","innhold":"Utland"}]},"norge":{"inntektAaretFoerDoedsfall":{"spoersmaal":"Hva var brutto årsinntekt året før dødsfallet?","svar":{"innhold":"500000"}},"inntektIFjor":{"tilDoedsfall":{"spoersmaal":"Hva var brutto arbeidsinntekt frem til dødsfallet?","svar":{"innhold":"500000"}},"aarsinntekt":{"spoersmaal":"Hva forventer du i brutto årsinntekt til neste år?","svar":{"innhold":"140 000"}}},"inntektIAar":{"tilDoedsfall":{"spoersmaal":"Hva var brutto arbeidsinntekt frem til dødsfallet?","svar":{"innhold":"500000"}},"aarsinntekt":{"spoersmaal":"Hva forventer du i brutto årsinntekt til neste år?","svar":{"innhold":"140 000"}}}},"utland":{"inntektAaretFoerDoedsfall":{"spoersmaal":"Hva var brutto årsinntekt året før dødsfallet?","svar":{"innhold":"500000"}},"inntektIFjor":{"tilDoedsfall":{"spoersmaal":"Hva var brutto arbeidsinntekt frem til dødsfallet?","svar":{"innhold":"500000"}},"aarsinntekt":{"spoersmaal":"Hva forventer du i brutto årsinntekt til neste år?","svar":{"innhold":"140 000"}}},"inntektIAar":{"tilDoedsfall":{"spoersmaal":"Hva var brutto arbeidsinntekt frem til dødsfallet?","svar":{"innhold":"500000"}},"aarsinntekt":{"spoersmaal":"Hva forventer du i brutto årsinntekt til neste år?","svar":{"innhold":"140 000"}}}},"endringAvInntekt":{"fremtidigEndringAvInntekt":{"spoersmaal":"Regner du med at inntekten din endrer seg fremover i tid?","svar":{"verdi":"VET_IKKE","innhold":"Vet ikke"}}}}},"naeringsinntekt":{"spoersmaal":"Næringsinntekt","svar":{"norgeEllerUtland":{"spoersmaal":"Hvor har du næringsinntekt fra?","svar":[{"verdi":"NORGE","innhold":"Norge"},{"verdi":"UTLAND","innhold":"Utland"}]},"norge":{"inntektAaretFoerDoedsfall":{"spoersmaal":"Hva var brutto årsinntekt året før dødsfallet?","svar":{"innhold":"500000"}},"inntektIFjor":{"tilDoedsfall":{"spoersmaal":"Hva var brutto arbeidsinntekt frem til dødsfallet?","svar":{"innhold":"500000"}},"aarsinntekt":{"spoersmaal":"Hva forventer du i brutto årsinntekt til neste år?","svar":{"innhold":"140 000"}}},"inntektIAar":{"tilDoedsfall":{"spoersmaal":"Hva var brutto arbeidsinntekt frem til dødsfallet?","svar":{"innhold":"500000"}},"aarsinntekt":{"spoersmaal":"Hva forventer du i brutto årsinntekt til neste år?","svar":{"innhold":"140 000"}}}},"utland":{"inntektAaretFoerDoedsfall":{"spoersmaal":"Hva var brutto årsinntekt året før dødsfallet?","svar":{"innhold":"500000"}},"inntektIFjor":{"tilDoedsfall":{"spoersmaal":"Hva var brutto arbeidsinntekt frem til dødsfallet?","svar":{"innhold":"500000"}},"aarsinntekt":{"spoersmaal":"Hva forventer du i brutto årsinntekt til neste år?","svar":{"innhold":"140 000"}}},"inntektIAar":{"tilDoedsfall":{"spoersmaal":"Hva var brutto arbeidsinntekt frem til dødsfallet?","svar":{"innhold":"500000"}},"aarsinntekt":{"spoersmaal":"Hva forventer du i brutto årsinntekt til neste år?","svar":{"innhold":"140 000"}}}},"endringAvInntekt":{"fremtidigEndringAvInntekt":{"spoersmaal":"Regner du med at inntekten din endrer seg fremover i tid?","svar":{"verdi":"JA","innhold":"Ja"}},"grunn":{"spoersmaal":"Hva er grunnen til endringene?","svar":{"verdi":"REDUSERT_STILLINGSPROSENT","innhold":"Redusert stillingsprosent"}}}}},"pensjonEllerUfoere":{"pensjonstype":{"spoersmaal":"Hvilken pensjon eller trygd har du?","svar":[{"verdi":"UFOEREPENSJON_FRA_NAV","innhold":"Uføretrygd fra NAV"},{"verdi":"ALDERSPENSJON_FRA_NAV","innhold":"Alderspensjon fra NAV"},{"verdi":"TJENESTEPENSJONSORDNING","innhold":"Pensjon fra tjenestepensjonsordning"}]},"tjenestepensjonsordning":{"type":{"spoersmaal":"Hva slags pensjon mottar du?","svar":{"verdi":"AVTALEFESTET_PENSJON_OFFENTLIG","innhold":"Avtalefestet pensjon i offentlig sektor"}},"utbetaler":{"spoersmaal":"Hvilken tjenestepensjonsordning utbetaler pensjonen din?","svar":{"innhold":"Satans Pensjonskasse"}}},"utland":{"type":{"spoersmaal":"Hva slags pensjon?","svar":{"innhold":"Ufør"}},"land":{"spoersmaal":"Hvilket land mottar du fra? ","svar":{"innhold":"Polen"}},"beloepMedValuta":{"spoersmaal":"Årlig beløp i landets valuta","svar":{"innhold":"4000 PLN"}}}},"inntektViaYtelserFraNAV":{"ytelser":{"spoersmaal":"Hvilke andre inntekter eller utbetalinger har du?","svar":[{"verdi":"DAGSPENGER","innhold":"Dagpenger"},{"verdi":"SVANGERSKAPSPENGER","innhold":"Svangerskapspenger"},{"verdi":"KOMMUNAL_OMSORGSSTOENAD","innhold":"Kommunal omsorgsstønad"}]}},"ytelserNAV":{"soektOmYtelse":{"spoersmaal":"Har du søkt om ytelser fra NAV som du ikke har fått svar på?","svar":{"verdi":"JA","innhold":"Ja"}},"soektYtelse":{"spoersmaal":"Hva har du søkt om?","svar":[{"verdi":"FORELDREPENGER","innhold":"Foreldrepenger"},{"verdi":"OMSORGSPENGER","innhold":"Omsorgspenger"},{"verdi":"FOSTERHJEMSGODTGJOERING","innhold":"Fosterhjemsgodtgjøring"}]}},"ytelserAndre":{"soektOmYtelse":{"spoersmaal":"Har du søkt om ytelser fra andre enn NAV som du ikke har fått svar på?","svar":{"verdi":"NEI","innhold":"Nei"}}}},"uregistrertEllerVenterBarn":{"spoersmaal":"Venter du barn eller har du barn som ikke er registrert i folkeregisteret?","svar":{"verdi":"NEI","innhold":"Nei"}},"forholdTilAvdoede":{"relasjon":{"spoersmaal":"Relasjonen din til avdøde da dødsfallet skjedde","svar":{"verdi":"GIFT","innhold":"Gift eller registrert partner"}},"datoForInngaattPartnerskap":{"spoersmaal":"Vi giftet oss","svar":{"innhold":"2005-12-01"}},"fellesBarn":{"spoersmaal":"Har eller har dere hatt felles barn?","svar":{"verdi":"JA","innhold":"Ja"}}},"omsorgForBarn":{"spoersmaal":"Har du minst 50 prosent omsorg for barn under 18 år på dødsfallstidspunktet?","svar":{"verdi":"JA","innhold":"Ja"}}},"avdoed":{"type":"AVDOED","fornavn":{"spoersmaal":"Fornavn","svar":"Overeksponert"},"etternavn":{"spoersmaal":"Etternavn","svar":"Mobiltelefon"},"foedselsnummer":{"spoersmaal":"Fødselsnummer / d-nummer","svar":"16498203950"},"datoForDoedsfallet":{"spoersmaal":"Når skjedde dødsfallet?","svar":{"innhold":"2023-11-20"}},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":{"innhold":"Norge"}},"utenlandsopphold":{"spoersmaal":"Har han eller hun bodd og/eller arbeidet i et annet land enn Norge etter fylte 16 år?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":[{"land":{"spoersmaal":"Land","svar":{"innhold":"Sverige"}},"fraDato":{"spoersmaal":"Fra dato (valgfri)","svar":{"innhold":"2003-07-22"}},"tilDato":{"spoersmaal":"Til dato (valgfri)","svar":{"innhold":"2003-12-31"}},"oppholdsType":{"spoersmaal":"Bodd og/eller arbeidet?","svar":[{"verdi":"BODD","innhold":"Bodd"},{"verdi":"ARBEIDET","innhold":"Arbeidet"}]},"medlemFolketrygd":{"spoersmaal":"Var han eller hun medlem av folketrygden under oppholdet?","svar":{"verdi":"JA","innhold":"Ja"}},"pensjonsutbetaling":{"spoersmaal":"Oppgi eventuell pensjon han eller hun mottok fra dette landet (valgfri)","svar":{"innhold":"140 000"}}}]},"naeringsInntekt":{"spoersmaal":"Var han eller hun selvstendig næringsdrivende?","svar":{"verdi":"NEI","innhold":"Nei"}},"doedsaarsakSkyldesYrkesskadeEllerYrkessykdom":{"spoersmaal":"Skyldes dødsfallet yrkesskade eller yrkessykdom?","svar":{"verdi":"NEI","innhold":"Nei"}}},"barn":[{"type":"BARN","fornavn":{"spoersmaal":"Fornavn","svar":"Innsiktsfull"},"etternavn":{"spoersmaal":"Etternavn","svar":"Koloni"},"foedselsnummer":{"spoersmaal":"Barnets fødselsnummer / d-nummer","svar":"24871899386"},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":"Norge"},"utenlandsAdresse":{"spoersmaal":"Bor barnet i et annet land enn Norge?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"land":{"spoersmaal":"Land","svar":{"innhold":"Danmark"}},"adresse":{"spoersmaal":"Adresse i utlandet","svar":{"innhold":"Kamelåså"}}}},"foreldre":[{"type":"FORELDER","fornavn":{"spoersmaal":"Fornavn","svar":"KALD"},"etternavn":{"spoersmaal":"Etternavn","svar":"FOLK"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"24876696580"}},{"type":"FORELDER","fornavn":{"spoersmaal":"Fornavn","svar":"Overeksponert"},"etternavn":{"spoersmaal":"Etternavn","svar":"Mobiltelefon"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"16498203950"}}],"verge":{"spoersmaal":"Er det oppnevnt en verge for barnet?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"type":"VERGE","fornavn":{"spoersmaal":"Fornavn","svar":"Verg"},"etternavn":{"spoersmaal":"Etternavn","svar":"Vikernes"}}}}],"andreStoenader":[],"mottattDato":"2023-11-30T18:05:49.337092713","template":"omstillingsstoenad_v2"},{"imageTag":"d79803f3acb657cf657ee46af1db293a665eb0d2","spraak":"nb","innsender":{"fornavn":{"svar":"TRADISJONSBUNDEN","spoersmaal":"Fornavn"},"etternavn":{"svar":"KØYESENG","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"13848599411","spoersmaal":"Fødselsnummer / d-nummer"},"type":"INNSENDER"},"harSamtykket":{"svar":true,"spoersmaal":"Jeg, TRADISJONSBUNDEN KØYESENG, bekrefter at jeg vil gi riktige og fullstendige opplysninger."},"utbetalingsInformasjon":{"svar":{"verdi":"NORSK","innhold":"Norsk"},"spoersmaal":"Ønsker du å motta utbetalingen på norsk eller utenlandsk bankkonto?","opplysning":{"kontonummer":{"svar":{"innhold":"1231.23.13137"},"spoersmaal":"Oppgi norsk kontonummer for utbetaling av barnepensjon"},"utenlandskBankNavn":null,"utenlandskBankAdresse":null,"iban":null,"swift":null,"skattetrekk":{"svar":{"svar":{"verdi":"NEI","innhold":"Nei"},"spoersmaal":"Ønsker du at vi legger inn et skattetrekk for barnepensjonen?"}}}},"soeker":{"fornavn":{"svar":"Blaut","spoersmaal":"Fornavn"},"etternavn":{"svar":"Sandkasse","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"19021370870","spoersmaal":"Fødselsnummer / d-nummer"},"statsborgerskap":{"svar":"Norge","spoersmaal":"Statsborgerskap"},"utenlandsAdresse":{"svar":{"verdi":"NEI","innhold":"Nei"},"spoersmaal":"Bor barnet i et annet land enn Norge?","opplysning":null},"bosattNorge":null,"foreldre":[{"fornavn":{"svar":"TRADISJONSBUNDEN","spoersmaal":"Fornavn"},"etternavn":{"svar":"KØYESENG","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"13848599411","spoersmaal":"Fødselsnummer / d-nummer"},"type":"FORELDER"},{"fornavn":{"svar":"TREG","spoersmaal":"Fornavn"},"etternavn":{"svar":"BILDE","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"03428317423","spoersmaal":"Fødselsnummer / d-nummer"},"type":"FORELDER"}],"ukjentForelder":null,"verge":{"svar":{"verdi":"NEI","innhold":"Nei"},"spoersmaal":"Er det oppnevnt en verge for barnet?","opplysning":null},"dagligOmsorg":null,"type":"BARN"},"foreldre":[{"fornavn":{"svar":"TRADISJONSBUNDEN","spoersmaal":"Fornavn"},"etternavn":{"svar":"KØYESENG","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"13848599411","spoersmaal":"Fødselsnummer / d-nummer"},"adresse":{"svar":"Tonnesveien 275, 8750 Tonnes","spoersmaal":"Bostedsadresse"},"statsborgerskap":{"svar":"Norge","spoersmaal":"Statsborgerskap"},"kontaktinfo":{"telefonnummer":{"svar":{"innhold":"+4799999999"},"spoersmaal":"Telefonnummer"}},"type":"GJENLEVENDE_FORELDER"},{"fornavn":{"svar":"TREG","spoersmaal":"Fornavn"},"etternavn":{"svar":"BILDE","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"03428317423","spoersmaal":"Fødselsnummer / d-nummer"},"datoForDoedsfallet":{"svar":{"innhold":"2023-11-01"},"spoersmaal":"Når skjedde dødsfallet?"},"statsborgerskap":{"svar":{"innhold":"Norge"},"spoersmaal":"Statsborgerskap"},"utenlandsopphold":{"svar":{"verdi":"NEI","innhold":"Nei"},"spoersmaal":"Har han eller hun bodd og/eller arbeidet i et annet land enn Norge etter fylte 16 år?","opplysning":null},"doedsaarsakSkyldesYrkesskadeEllerYrkessykdom":{"svar":{"verdi":"NEI","innhold":"Nei"},"spoersmaal":"Skyldes dødsfallet yrkesskade eller yrkessykdom?"},"naeringsInntekt":{"svar":{"verdi":"NEI","innhold":"Nei"},"spoersmaal":"Var han eller hun selvstendig næringsdrivende?","opplysning":null},"militaertjeneste":null,"type":"AVDOED"}],"soesken":[],"versjon":"2","type":"BARNEPENSJON","mottattDato":"2023-11-30T18:05:49.337092713","template":"barnepensjon_v2"}]}"""

        val deserialized = mapper.readValue(json, jacksonTypeRef<SoeknadRequest>())

        deserialized.soeknader.size shouldBeExactly 2

        val omstillingsstoenad = deserialized.soeknader.first()
        omstillingsstoenad.type shouldBe SoeknadType.OMSTILLINGSSTOENAD
        omstillingsstoenad.template() shouldBe "omstillingsstoenad_v1"

        val barnepensjon = deserialized.soeknader.last()
        barnepensjon.type shouldBe SoeknadType.BARNEPENSJON
        barnepensjon.template() shouldBe "barnepensjon_v2"
    }

    @Test
    fun `Deserialisering av barnepensjon`() {
        val json = javaClass.getResource("/soeknad/barnepensjon.json")!!.readText()

        val soeknad = mapper.readValue<InnsendtSoeknad>(json)
        assertTrue(soeknad is Barnepensjon)
    }

    @Test
    fun `Deserialisering av barnepensjon utland`() {
        val json = javaClass.getResource("/soeknad/barnepensjon_utland.json")!!.readText()

        val soeknad = mapper.readValue<InnsendtSoeknad>(json)
        assertTrue(soeknad is Barnepensjon)
    }

    @Test
    fun `Deserialisering av omstillingsstoenad`() {
        val json = javaClass.getResource("/soeknad/omstillingsstoenad.json")!!.readText()

        val soeknad = mapper.readValue<InnsendtSoeknad>(json)
        assertTrue(soeknad is Omstillingsstoenad)
    }
}
