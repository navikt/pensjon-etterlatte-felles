package innsendtsoeknad.common

import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.etterlatte.libs.common.innsendtsoeknad.EndringAvInntektGrunnType
import no.nav.etterlatte.libs.common.innsendtsoeknad.StudieformType
import no.nav.etterlatte.libs.common.innsendtsoeknad.barnepensjon.Barnepensjon
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Behandlingsnummer
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.InnsendtSoeknad
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.JaNeiVetIkke
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.PersonType
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.SoeknadRequest
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.SoeknadType
import no.nav.etterlatte.libs.common.innsendtsoeknad.omstillingsstoenad.Omstillingsstoenad
import no.nav.etterlatte.libs.common.innsendtsoeknad.utvidetomstillingsstoenad.NySivilstandValg
import no.nav.etterlatte.libs.common.innsendtsoeknad.utvidetomstillingsstoenad.TiltakOmfangType
import no.nav.etterlatte.libs.common.innsendtsoeknad.utvidetomstillingsstoenad.UtdanningOgTiltakValg
import no.nav.etterlatte.libs.common.innsendtsoeknad.utvidetomstillingsstoenad.UtvidetOmstillingsstoenad
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tools.jackson.module.kotlin.jacksonObjectMapper
import tools.jackson.module.kotlin.jacksonTypeRef
import tools.jackson.module.kotlin.readValue
import java.time.LocalDate

@Suppress("ktlint:standard:max-line-length")
internal class InnsendtSoeknadTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `Deserialisering fungerer som forventet`() {
        val json = """{"soeknader":[{"imageTag":"d79803f3acb657cf657ee46af1db293a665eb0d2","spraak":"nb","type":"OMSTILLINGSSTOENAD","harSamtykket":{"spoersmaal":"Jeg vil svare så godt jeg kan på spørsmålene i søknaden.","svar":true},"innsender":{"type":"INNSENDER","fornavn":{"spoersmaal":"Fornavn","svar":"KALD"},"etternavn":{"spoersmaal":"Etternavn","svar":"FOLK"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"24876696580"}},"utbetalingsInformasjon":{"spoersmaal":"Ønsker du å motta utbetalingen på norsk eller utenlandsk bankkonto?","svar":{"verdi":"NORSK","innhold":"Norsk"},"opplysning":{"kontonummer":{"spoersmaal":"Oppgi norsk kontonummer for utbetaling","svar":{"innhold":"1241.24.12412"}}}},"soeker":{"type":"GJENLEVENDE_OMS","fornavn":{"spoersmaal":"Fornavn","svar":"KALD"},"etternavn":{"spoersmaal":"Etternavn","svar":"FOLK"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"24876696580"},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":"Norge"},"sivilstatus":{"spoersmaal":"Sivilstatus","svar":"Gift"},"adresse":{"spoersmaal":"Bostedsadresse","svar":"Nedre Ovrå 28, 6212 Liabygda"},"kontaktinfo":{"telefonnummer":{"spoersmaal":"Telefonnummer","svar":{"innhold":"+4799999999"}}},"oppholdUtland":{"spoersmaal":"Er du bosatt i Norge?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"oppholderSegIUtlandet":{"spoersmaal":"Har du bodd eller oppholdt deg i utlandet de siste 12 månedene?","svar":{"verdi":"NEI","innhold":"Nei"}}}},"nySivilstatus":{"spoersmaal":"Sivilstanden din i dag","svar":{"verdi":"ENSLIG","innhold":"Enslig"}},"arbeidOgUtdanning":{"dinSituasjon":{"spoersmaal":"Hva er situasjonen din nå?","svar":[{"verdi":"ARBEIDSTAKER","innhold":"Jeg er arbeidstaker og/eller lønnsmottaker som frilanser"}]},"arbeidsforhold":{"spoersmaal":"Om arbeidsforholdet ditt","svar":[{"arbeidsgiver":{"spoersmaal":"Navn på arbeidssted","svar":{"innhold":"NAV"}},"arbeidsmengde":{"spoersmaal":"Fyll ut stillingsprosenten din","svar":{"innhold":"100% Prosent"}},"ansettelsesforhold":{"spoersmaal":"Hva slags type ansettelsesforhold har du?","svar":{"verdi":"FAST","innhold":"Fast ansatt"}},"endretArbeidssituasjon":{"spoersmaal":"Forventer du endringer i arbeidsforholdet ditt fremover i tid?","svar":{"verdi":"NEI","innhold":"Nei"}}},{"arbeidsgiver":{"spoersmaal":"Navn på arbeidssted","svar":{"innhold":"EH"}},"arbeidsmengde":{"spoersmaal":"Fyll ut stillingsprosenten din","svar":{"innhold":"30 Prosent"}},"ansettelsesforhold":{"spoersmaal":"Hva slags type ansettelsesforhold har du?","svar":{"verdi":"FAST","innhold":"Fast ansatt"}},"endretArbeidssituasjon":{"spoersmaal":"Forventer du endringer i arbeidsforholdet ditt fremover i tid?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"spoersmaal":"Gi en kort beskrivelse av endringene","svar":{"innhold":"Hmmm"}}}}]}},"fullfoertUtdanning":{"spoersmaal":"Hva er din høyeste fullførte utdanning?","svar":[{"verdi":"UNIVERSITET_OVER_4_AAR","innhold":"Universitet eller høyskole mer enn 4 år"},{"verdi":"FAGBREV","innhold":"Fagbrev"},{"verdi":"ANNEN","innhold":"Annen utdanning"}]},"inntektOgPensjon":{"skalGaaAvMedAlderspensjon":{"valg":{"spoersmaal":"Skal du gå av med alderspensjon i år?","svar":{"verdi":"JA","innhold":"Ja"}},"datoForAaGaaAvMedAlderspensjon":{"spoersmaal":"Når planlegger du å ta ut alderspensjon?","svar":{"innhold":"2024-12-03"}}},"inntektFremTilDoedsfallet":{"spoersmaal":"Inntekt frem til dødsfallet","svar":{"arbeidsinntekt":{"spoersmaal":"Arbeidsinntekt og andre utbetalinger","svar":{"innhold":"123"}},"naeringsinntekt":{"inntekt":{"spoersmaal":"Næringsinntekt","svar":{"innhold":"123"}},"erNaeringsinntektOpptjentJevnt":{"valg":{"spoersmaal":"Er næringsinntekten opptjent jevnt i løpet av året?","svar":{"verdi":"NEI","innhold":"Nei"}},"beskrivelse":{"spoersmaal":"Skriv kort om hvordan inntekten varierer gjennom året","svar":{"innhold":"Den varierer veldig"}}}},"afpInntekt":{"inntekt":{"spoersmaal":"Avtalefestet pensjon (AFP) offentlig eller privat","svar":{"innhold":"123"}},"tjenesteordning":{"spoersmaal":"Hvilken tjenesteordning får du AFP fra?","svar":{"innhold":"KLP"}}},"inntektFraUtland":{"spoersmaal":"Alle inntekter fra utland","svar":{"innhold":"123"}},"andreInntekter":{"valg":{"spoersmaal":"Hadde du andre inntekter?","svar":{"verdi":"JA","innhold":"Ja"}},"inntekt":{"spoersmaal":"Andre inntekter","svar":{"innhold":"123"}},"beskrivelse":{"spoersmaal":"Hva slags inntekt var det?","svar":{"innhold":"En hel haug av andre inntekter"}}}}},"forventetInntektIAar":{"spoersmaal":"Forventet årsinntekt i år","svar":{"arbeidsinntekt":{"spoersmaal":"Arbeidsinntekt og andre utbetalinger","svar":{"innhold":"123"}},"naeringsinntekt":{"inntekt":{"spoersmaal":"Næringsinntekt","svar":{"innhold":"123"}},"erNaeringsinntektOpptjentJevnt":{"valg":{"spoersmaal":"Er næringsinntekten opptjent jevnt i løpet av året?","svar":{"verdi":"NEI","innhold":"Nei"}},"beskrivelse":{"spoersmaal":"Skriv kort om hvordan inntekten varierer gjennom året","svar":{"innhold":"Den varierer veldig"}}}},"afpInntekt":{"inntekt":{"spoersmaal":"Avtalefestet pensjon (AFP) offentlig eller privat","svar":{"innhold":"123"}},"tjenesteordning":{"spoersmaal":"Hvilken tjenesteordning får du AFP fra?","svar":{"innhold":"KLP"}}},"inntektFraUtland":{"spoersmaal":"Alle inntekter fra utland","svar":{"innhold":"123"}},"andreInntekter":{"valg":{"spoersmaal":"Har du andre inntekter?","svar":{"verdi":"JA","innhold":"Ja"}},"inntekt":{"spoersmaal":"Andre inntekter","svar":{"innhold":"123"}},"beskrivelse":{"spoersmaal":"Hva slags inntekt er det?","svar":{"innhold":"Masse rare intekter"}}},"noeSomKanPaavirkeInntekten":{"valg":{"spoersmaal":"Er det noe du vet om i dag som kan påvirke inntekten din fremover?","svar":{"verdi":"JA","innhold":"Ja"}},"grunnTilPaavirkelseAvInntekt":{"spoersmaal":"Hva er grunnen til at inntekten endrer seg?","svar":{"verdi":"ANNEN_GRUNN","innhold":"Annen grunn"}},"beskrivelse":{"spoersmaal":"Beskriv endringen","svar":{"innhold":"Endringen endrer seg endelig"}}}}},"forventetInntektTilNesteAar":{"spoersmaal":"Forventet inntekt til neste år","svar":{"arbeidsinntekt":{"spoersmaal":"Arbeidsinntekt og andre utbetalinger","svar":{"innhold":"123"}},"naeringsinntekt":{"inntekt":{"spoersmaal":"Næringsinntekt","svar":{"innhold":"123"}},"erNaeringsinntektOpptjentJevnt":{"valg":{"spoersmaal":"Er næringsinntekten opptjent jevnt i løpet av året?","svar":{"verdi":"NEI","innhold":"Nei"}},"beskrivelse":{"spoersmaal":"Skriv kort om hvordan inntekten varierer gjennom året","svar":{"innhold":"Den varierer veldig"}}}},"afpInntekt":{"inntekt":{"spoersmaal":"Avtalefestet pensjon (AFP) offentlig eller privat","svar":{"innhold":"123"}},"tjenesteordning":{"spoersmaal":"Hvilken tjenesteordning får du AFP fra?","svar":{"innhold":"KLP"}}},"inntektFraUtland":{"spoersmaal":"Alle inntekter fra utland","svar":{"innhold":"123"}},"andreInntekter":{"valg":{"spoersmaal":"Har du andre inntekter?","svar":{"verdi":"JA","innhold":"Ja"}},"inntekt":{"spoersmaal":"Andre inntekter","svar":{"innhold":"123"}},"beskrivelse":{"spoersmaal":"Hva slags inntekt er det?","svar":{"innhold":"Masse rare intekter"}}},"noeSomKanPaavirkeInntekten":{"valg":{"spoersmaal":"Er det noe du vet om i dag som kan påvirke inntekten din fremover?","svar":{"verdi":"JA","innhold":"Ja"}},"grunnTilPaavirkelseAvInntekt":{"spoersmaal":"Hva er grunnen til at inntekten endrer seg?","svar":{"verdi":"ANNEN_GRUNN","innhold":"Annen grunn"}},"beskrivelse":{"spoersmaal":"Beskriv endringen","svar":{"innhold":"Endringen endrer seg endelig"}}}}},"ytelserNAV":{"soektOmYtelse":{"spoersmaal":"Har du søkt om ytelser fra NAV som du ikke har fått svar på?","svar":{"verdi":"JA","innhold":"Ja"}},"soektYtelse":{"spoersmaal":"Hva har du søkt om?","svar":[{"verdi":"FORELDREPENGER","innhold":"Foreldrepenger"},{"verdi":"OMSORGSPENGER","innhold":"Omsorgspenger"},{"verdi":"FOSTERHJEMSGODTGJOERING","innhold":"Fosterhjemsgodtgjøring"}]}},"ytelserAndre":{"soektOmYtelse":{"spoersmaal":"Har du søkt om ytelser fra andre enn NAV som du ikke har fått svar på?","svar":{"verdi":"NEI","innhold":"Nei"}}}},"uregistrertEllerVenterBarn":{"spoersmaal":"Venter du barn eller har du barn som ikke er registrert i folkeregisteret?","svar":{"verdi":"NEI","innhold":"Nei"}},"forholdTilAvdoede":{"relasjon":{"spoersmaal":"Relasjonen din til avdøde da dødsfallet skjedde","svar":{"verdi":"GIFT","innhold":"Gift eller registrert partner"}},"datoForInngaattPartnerskap":{"spoersmaal":"Vi giftet oss","svar":{"innhold":"2005-12-01"}},"fellesBarn":{"spoersmaal":"Har eller har dere hatt felles barn?","svar":{"verdi":"JA","innhold":"Ja"}}},"omsorgForBarn":{"spoersmaal":"Har du minst 50 prosent omsorg for barn under 18 år på dødsfallstidspunktet?","svar":{"verdi":"JA","innhold":"Ja"}}},"avdoed":{"type":"AVDOED","fornavn":{"spoersmaal":"Fornavn","svar":"Overeksponert"},"etternavn":{"spoersmaal":"Etternavn","svar":"Mobiltelefon"},"foedselsnummer":{"spoersmaal":"Fødselsnummer / d-nummer","svar":"16498203950"},"datoForDoedsfallet":{"spoersmaal":"Når skjedde dødsfallet?","svar":{"innhold":"2023-11-20"}},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":{"innhold":"Norge"}},"utenlandsopphold":{"spoersmaal":"Har han eller hun bodd og/eller arbeidet i et annet land enn Norge etter fylte 16 år?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":[{"land":{"spoersmaal":"Land","svar":{"innhold":"Sverige"}},"fraDato":{"spoersmaal":"Fra dato (valgfri)","svar":{"innhold":"2003-07-22"}},"tilDato":{"spoersmaal":"Til dato (valgfri)","svar":{"innhold":"2003-12-31"}},"oppholdsType":{"spoersmaal":"Bodd og/eller arbeidet?","svar":[{"verdi":"BODD","innhold":"Bodd"},{"verdi":"ARBEIDET","innhold":"Arbeidet"}]},"medlemFolketrygd":{"spoersmaal":"Var han eller hun medlem av folketrygden under oppholdet?","svar":{"verdi":"JA","innhold":"Ja"}},"pensjonsutbetaling":{"spoersmaal":"Oppgi eventuell pensjon han eller hun mottok fra dette landet (valgfri)","svar":{"innhold":"140 000"}}}]},"doedsaarsakSkyldesYrkesskadeEllerYrkessykdom":{"spoersmaal":"Skyldes dødsfallet yrkesskade eller yrkessykdom?","svar":{"verdi":"NEI","innhold":"Nei"}}},"barn":[{"type":"BARN","fornavn":{"spoersmaal":"Fornavn","svar":"Innsiktsfull"},"etternavn":{"spoersmaal":"Etternavn","svar":"Koloni"},"foedselsnummer":{"spoersmaal":"Barnets fødselsnummer / d-nummer","svar":"24871899386"},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":"Norge"},"utenlandsAdresse":{"spoersmaal":"Bor barnet i et annet land enn Norge?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"land":{"spoersmaal":"Land","svar":{"innhold":"Danmark"}},"adresse":{"spoersmaal":"Adresse i utlandet","svar":{"innhold":"Kamelåså"}}}},"foreldre":[{"type":"FORELDER","fornavn":{"spoersmaal":"Fornavn","svar":"KALD"},"etternavn":{"spoersmaal":"Etternavn","svar":"FOLK"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"24876696580"}},{"type":"FORELDER","fornavn":{"spoersmaal":"Fornavn","svar":"Overeksponert"},"etternavn":{"spoersmaal":"Etternavn","svar":"Mobiltelefon"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"16498203950"}}],"verge":{"spoersmaal":"Er det oppnevnt en verge for barnet?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"type":"VERGE","fornavn":{"spoersmaal":"Fornavn","svar":"Verg"},"etternavn":{"spoersmaal":"Etternavn","svar":"Vikernes"}}}}],"andreStoenader":[],"mottattDato":"2023-11-30T18:05:49.337092713","template":"omstillingsstoenad_v2"},{"imageTag":"d79803f3acb657cf657ee46af1db293a665eb0d2","spraak":"nb","innsender":{"fornavn":{"svar":"TRADISJONSBUNDEN","spoersmaal":"Fornavn"},"etternavn":{"svar":"KØYESENG","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"13848599411","spoersmaal":"Fødselsnummer / d-nummer"},"type":"INNSENDER"},"harSamtykket":{"svar":true,"spoersmaal":"Jeg, TRADISJONSBUNDEN KØYESENG, bekrefter at jeg vil gi riktige og fullstendige opplysninger."},"utbetalingsInformasjon":{"svar":{"verdi":"NORSK","innhold":"Norsk"},"spoersmaal":"Ønsker du å motta utbetalingen på norsk eller utenlandsk bankkonto?","opplysning":{"kontonummer":{"svar":{"innhold":"1231.23.13137"},"spoersmaal":"Oppgi norsk kontonummer for utbetaling av barnepensjon"},"utenlandskBankNavn":null,"utenlandskBankAdresse":null,"iban":null,"swift":null}},"soeker":{"fornavn":{"svar":"Blaut","spoersmaal":"Fornavn"},"etternavn":{"svar":"Sandkasse","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"19021370870","spoersmaal":"Fødselsnummer / d-nummer"},"statsborgerskap":{"svar":"Norge","spoersmaal":"Statsborgerskap"},"utenlandsAdresse":{"svar":{"verdi":"NEI","innhold":"Nei"},"spoersmaal":"Bor barnet i et annet land enn Norge?","opplysning":null},"bosattNorge":null,"foreldre":[{"fornavn":{"svar":"TRADISJONSBUNDEN","spoersmaal":"Fornavn"},"etternavn":{"svar":"KØYESENG","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"13848599411","spoersmaal":"Fødselsnummer / d-nummer"},"type":"FORELDER"},{"fornavn":{"svar":"TREG","spoersmaal":"Fornavn"},"etternavn":{"svar":"BILDE","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"03428317423","spoersmaal":"Fødselsnummer / d-nummer"},"type":"FORELDER"}],"ukjentForelder":null,"verge":{"svar":{"verdi":"NEI","innhold":"Nei"},"spoersmaal":"Er det oppnevnt en verge for barnet?","opplysning":null},"dagligOmsorg":null,"type":"BARN"},"foreldre":[{"fornavn":{"svar":"TRADISJONSBUNDEN","spoersmaal":"Fornavn"},"etternavn":{"svar":"KØYESENG","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"13848599411","spoersmaal":"Fødselsnummer / d-nummer"},"adresse":{"svar":"Tonnesveien 275, 8750 Tonnes","spoersmaal":"Bostedsadresse"},"statsborgerskap":{"svar":"Norge","spoersmaal":"Statsborgerskap"},"kontaktinfo":{"telefonnummer":{"svar":{"innhold":"+4799999999"},"spoersmaal":"Telefonnummer"}},"type":"GJENLEVENDE_FORELDER"},{"fornavn":{"svar":"TREG","spoersmaal":"Fornavn"},"etternavn":{"svar":"BILDE","spoersmaal":"Etternavn"},"foedselsnummer":{"svar":"03428317423","spoersmaal":"Fødselsnummer / d-nummer"},"datoForDoedsfallet":{"svar":{"innhold":"2023-11-01"},"spoersmaal":"Når skjedde dødsfallet?"},"statsborgerskap":{"svar":{"innhold":"Norge"},"spoersmaal":"Statsborgerskap"},"utenlandsopphold":{"svar":{"verdi":"NEI","innhold":"Nei"},"spoersmaal":"Har han eller hun bodd og/eller arbeidet i et annet land enn Norge etter fylte 16 år?","opplysning":null},"doedsaarsakSkyldesYrkesskadeEllerYrkessykdom":{"svar":{"verdi":"NEI","innhold":"Nei"},"spoersmaal":"Skyldes dødsfallet yrkesskade eller yrkessykdom?"},"militaertjeneste":null,"type":"AVDOED"}],"soesken":[],"versjon":"2","type":"BARNEPENSJON","mottattDato":"2023-11-30T18:05:49.337092713","template":"barnepensjon_v2"}]}"""

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

    @Test
    fun `Deserialisering av utvidet omstillingsstoenad med utdanning`() {
        val soeknad = lesUtvidetOms("/soeknad/utvidet_omstillingsstoenad.json")

        soeknad.type shouldBe SoeknadType.UTVIDET_OMSTILLINGSSTOENAD
        soeknad.type.behandlingsnummer shouldBe Behandlingsnummer.OMSTILLINGSSTOENAD
        soeknad.template() shouldBe "utvidet_omstillingsstoenad_v1"
        soeknad.soeker.type shouldBe PersonType.GJENLEVENDE_UTVIDET_OMS
        soeknad.utbetalingsInformasjon shouldBe null

        soeknad.situasjonenDinIDag.nySivilstand.svar.verdi shouldBe NySivilstandValg.INGEN_AV_DELENE
        soeknad.situasjonenDinIDag.nySivilstand.opplysning shouldBe null

        with(soeknad.utdanningOgTiltak) {
            aktivitet.svar.verdi shouldBe UtdanningOgTiltakValg.UTDANNING
            arbeidsrettetTiltak shouldBe null
            aktivitetsplan!!.svar.verdi shouldBe JaNeiVetIkke.JA
            utdanning!!.studieform.svar.verdi shouldBe StudieformType.DELTID
            utdanning.studieprosent!!.svar.innhold shouldBe "60"
            utdanning.startDato.svar.innhold shouldBe LocalDate.of(2026, 8, 15)
        }

        soeknad.inntekt.arbeidsinntekt!!.svar.innhold shouldBe "250000"
        soeknad.inntekt.afpInntekt shouldBe null
    }

    @Test
    fun `Deserialisering av utvidet omstillingsstoenad med arbeidsrettet tiltak`() {
        val soeknad = lesUtvidetOms("/soeknad/utvidet_omstillingsstoenad_tiltak.json")

        with(soeknad.utdanningOgTiltak) {
            aktivitet.svar.verdi shouldBe UtdanningOgTiltakValg.ARBEIDSRETTET_TILTAK
            utdanning shouldBe null
            arbeidsrettetTiltak!!.omfang.svar.verdi shouldBe TiltakOmfangType.HELTID
            arbeidsrettetTiltak.tiltak.svar.innhold shouldBe "Arbeidstrening"
        }
        soeknad.inntekt.noeSomKanPaavirkeInntekten!!.grunnTilPaavirkelseAvInntekt!!.svar.verdi shouldBe
            EndringAvInntektGrunnType.ANNEN_GRUNN
    }

    @Test
    fun `Deserialisering av utvidet omstillingsstoenad med ny samboer`() {
        val soeknad = lesUtvidetOms("/soeknad/utvidet_omstillingsstoenad_ny_samboer.json")

        soeknad.situasjonenDinIDag.nySivilstand.svar.verdi shouldBe NySivilstandValg.NY_SAMBOER
        val samboer = soeknad.situasjonenDinIDag.nySivilstand.opplysning!!
        samboer.foedselsnummer.svar.value shouldBe "13848599411"
        samboer.fellesBarnEllertidligereGift.svar.verdi shouldBe JaNeiVetIkke.NEI
        samboer.inntekt shouldBe null

        with(soeknad.utdanningOgTiltak) {
            aktivitet.svar.verdi shouldBe UtdanningOgTiltakValg.INGEN
            utdanning shouldBe null
            arbeidsrettetTiltak shouldBe null
            aktivitetsplan shouldBe null
        }
    }

    @Test
    fun `Utvidet omstillingsstoenad overlever serialisering og deserialisering`() {
        listOf(
            "/soeknad/utvidet_omstillingsstoenad.json",
            "/soeknad/utvidet_omstillingsstoenad_tiltak.json",
            "/soeknad/utvidet_omstillingsstoenad_ny_samboer.json",
        ).forEach { fil ->
            val original = lesUtvidetOms(fil)

            val serialisert = mapper.writeValueAsString(original)
            serialisert shouldContain "\"template\":\"utvidet_omstillingsstoenad_v1\""

            val request = mapper.readValue<SoeknadRequest>("""{"soeknader":[$serialisert]}""")
            request.soeknader.single() shouldBe original
        }
    }

    private fun lesUtvidetOms(fil: String): UtvidetOmstillingsstoenad {
        val json = javaClass.getResource(fil)!!.readText()
        return mapper.readValue<InnsendtSoeknad>(json).shouldBeInstanceOf<UtvidetOmstillingsstoenad>()
    }
}
