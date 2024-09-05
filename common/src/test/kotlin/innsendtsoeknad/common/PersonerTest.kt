package innsendtsoeknad.common

import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDate
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Avdoed
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Barn
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.JaNeiVetIkke
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Opplysning
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Person
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.Verge
import no.nav.etterlatte.libs.common.person.Foedselsnummer
import org.junit.jupiter.api.Test

@Suppress("ktlint:standard:max-line-length")
internal class PersonerTest {
    private val mapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())

    @Test
    fun `Serde av verge fungerer`() {
        val verge =
            Verge(
                Opplysning("Fornavn"),
                Opplysning("Etternavn"),
                Opplysning(Foedselsnummer.of("24014021406")),
            )

        val serialized = mapper.writeValueAsString(verge)

        val deserialized = mapper.readValue(serialized, jacksonTypeRef<Verge>())

        (deserialized is Verge) shouldBe true
        deserialized.fornavn shouldBe verge.fornavn
        deserialized.etternavn shouldBe verge.etternavn
        deserialized.foedselsnummer shouldBe verge.foedselsnummer
    }

    @Test
    fun `Serde av verge fungerer med nullable felter`() {
        val verge =
            Verge(
                fornavn = Opplysning("Fornavn"),
            )

        val serialized = mapper.writeValueAsString(verge)

        val deserialized = mapper.readValue(serialized, jacksonTypeRef<Verge>())

        (deserialized is Verge) shouldBe true
        deserialized.fornavn shouldBe verge.fornavn
        deserialized.etternavn shouldBe null
        deserialized.foedselsnummer shouldBe null
    }

    @Test
    fun `Deserialisering av barn fungerer`() {
        val barnJson = """{"type":"BARN","fornavn":{"spoersmaal":"Fornavn","svar":"Blåøyd"},"etternavn":{"spoersmaal":"Etternavn","svar":"Saks"},"foedselsnummer":{"spoersmaal":"Barnets fødselsnummer / d-nummer","svar":"09011350027"},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":"Norge"},"utenlandsAdresse":{"spoersmaal":"Bor barnet i et annet land enn Norge?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"land":{"spoersmaal":"Land","svar":{"innhold":"Polen"}},"adresse":{"spoersmaal":"Adresse i utlandet","svar":{"innhold":"Polski gatski 13"}}}},"foreldre":[{"type":"FORELDER","fornavn":{"spoersmaal":"Fornavn","svar":"STOR"},"etternavn":{"spoersmaal":"Etternavn","svar":"SNERK"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"11057523044"}},{"type":"FORELDER","fornavn":{"spoersmaal":"Fornavn","svar":"Polski"},"etternavn":{"spoersmaal":"Etternavn","svar":"Dødski"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"26104500284"}}],"verge":{"spoersmaal":"Er det oppnevnt en verge for barnet?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"type":"VERGE","fornavn":{"spoersmaal":"Fornavn","svar":"Verg"},"etternavn":{"spoersmaal":"Etternavn","svar":"Vikernes"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"30106519672"}}}}"""

        val deserialized = mapper.readValue(barnJson, jacksonTypeRef<Person>())

        (deserialized as Barn) shouldNotBe null
        deserialized.fornavn.svar shouldBe "Blåøyd"
        deserialized.etternavn.svar shouldBe "Saks"
        deserialized.foedselsdato shouldBe null
        deserialized.foedselsnummer!!.svar.value shouldBe "09011350027"

        deserialized.foreldre.size shouldBe 2
        deserialized.dagligOmsorg?.svar shouldBe null
        deserialized.statsborgerskap.svar shouldBe "Norge"
        deserialized.utenlandsAdresse!!.svar.verdi shouldBe JaNeiVetIkke.JA
    }

    @Test
    fun `Deserialisering av barn, uten foedselsnummer`() {
        val barnJson = """{"type":"BARN","fornavn":{"spoersmaal":"Fornavn","svar":"Blåøyd"},"etternavn":{"spoersmaal":"Etternavn","svar":"Saks"},"foedselsnummer": null,"foedselsdato":{"spoersmaal":"Fødselsdato","svar":"2013-01-09"},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":"Norge"},"utenlandsAdresse":{"spoersmaal":"Bor barnet i et annet land enn Norge?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"land":{"spoersmaal":"Land","svar":{"innhold":"Polen"}},"adresse":{"spoersmaal":"Adresse i utlandet","svar":{"innhold":"Polski gatski 13"}}}},"foreldre":[{"type":"FORELDER","fornavn":{"spoersmaal":"Fornavn","svar":"STOR"},"etternavn":{"spoersmaal":"Etternavn","svar":"SNERK"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"11057523044"}},{"type":"FORELDER","fornavn":{"spoersmaal":"Fornavn","svar":"Polski"},"etternavn":{"spoersmaal":"Etternavn","svar":"Dødski"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"26104500284"}}],"verge":{"spoersmaal":"Er det oppnevnt en verge for barnet?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"type":"VERGE","fornavn":{"spoersmaal":"Fornavn","svar":"Verg"},"etternavn":{"spoersmaal":"Etternavn","svar":"Vikernes"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"30106519672"}}}}"""

        val deserialized = mapper.readValue(barnJson, jacksonTypeRef<Person>())

        (deserialized as Barn) shouldNotBe null
        deserialized.fornavn.svar shouldBe "Blåøyd"
        deserialized.etternavn.svar shouldBe "Saks"
        deserialized.foedselsnummer shouldBe null
        deserialized.foedselsdato!!.svar shouldBe LocalDate.of(2013, 1, 9)
    }

    @Test
    fun `Deserialisering av barn, uten verken foedselsnummer eller foedselsdato`() {
        val barnJson = """{"type":"BARN","fornavn":{"spoersmaal":"Fornavn","svar":"Blåøyd"},"etternavn":{"spoersmaal":"Etternavn","svar":"Saks"},"foedselsnummer":null,"foedselsdato":null,"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":"Norge"},"utenlandsAdresse":{"spoersmaal":"Bor barnet i et annet land enn Norge?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"land":{"spoersmaal":"Land","svar":{"innhold":"Polen"}},"adresse":{"spoersmaal":"Adresse i utlandet","svar":{"innhold":"Polski gatski 13"}}}},"foreldre":[{"type":"FORELDER","fornavn":{"spoersmaal":"Fornavn","svar":"STOR"},"etternavn":{"spoersmaal":"Etternavn","svar":"SNERK"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"11057523044"}},{"type":"FORELDER","fornavn":{"spoersmaal":"Fornavn","svar":"Polski"},"etternavn":{"spoersmaal":"Etternavn","svar":"Dødski"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"26104500284"}}],"verge":{"spoersmaal":"Er det oppnevnt en verge for barnet?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning":{"type":"VERGE","fornavn":{"spoersmaal":"Fornavn","svar":"Verg"},"etternavn":{"spoersmaal":"Etternavn","svar":"Vikernes"},"foedselsnummer":{"spoersmaal":"Fødselsnummer","svar":"30106519672"}}}}"""

        shouldThrow<Exception> {
            mapper.readValue(barnJson, jacksonTypeRef<Person>())
        }
    }

    @Test
    fun `Avdød med fødselsdato, uten fnr`() {
        val avdoedUtenFnrJson = """{"type":"AVDOED","fornavn":{"spoersmaal":"Fornavn","svar":"Overeksponert"},"etternavn":{"spoersmaal":"Etternavn","svar":"Mobiltelefon"},"foedselsnummer": null,"foedselsdato":{"spoersmaal":"Fødselsdato","svar":"1982-09-16"},"datoForDoedsfallet":{"spoersmaal":"Når skjedde dødsfallet?","svar":{"innhold":"2023-11-20"}},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":{"innhold":"Norge"}},"utenlandsopphold":{"spoersmaal":"Har han eller hun bodd og/eller arbeidet i et annet land enn Norge etter fylte 16 år?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning": [{"land":{"spoersmaal":"Land","svar":{"innhold":"Sverige"}},"fraDato":{"spoersmaal":"Fra dato (valgfri)","svar":{"innhold":"2003-07-22"}},"tilDato":{"spoersmaal":"Til dato (valgfri)","svar":{"innhold":"2003-12-31"}},"oppholdsType":{"spoersmaal":"Bodd og/eller arbeidet?","svar": [{"verdi":"BODD","innhold":"Bodd"},{"verdi":"ARBEIDET","innhold":"Arbeidet"}]},"medlemFolketrygd":{"spoersmaal":"Var han eller hun medlem av folketrygden under oppholdet?","svar":{"verdi":"JA","innhold":"Ja"}},"pensjonsutbetaling":{"spoersmaal":"Oppgi eventuell pensjon han eller hun mottok fra dette landet (valgfri)","svar":{"innhold":"140 000"}}}]},"naeringsInntekt":{"spoersmaal":"Var han eller hun selvstendig næringsdrivende?","svar":{"verdi":"NEI","innhold":"Nei"}},"doedsaarsakSkyldesYrkesskadeEllerYrkessykdom":{"spoersmaal":"Skyldes dødsfallet yrkesskade eller yrkessykdom?","svar":{"verdi":"NEI","innhold":"Nei"}}}"""

        val avdoedUtenFnr = mapper.readValue(avdoedUtenFnrJson, jacksonTypeRef<Person>())

        (avdoedUtenFnr is Avdoed) shouldBe true
        avdoedUtenFnr.foedselsnummer shouldBe null
        avdoedUtenFnr.foedselsdato?.svar shouldBe LocalDate.of(1982, 9, 16)
    }

    @Test
    fun `Avdød med fnr, uten fødselsdato`() {
        val avdoedUtenFdatoJson = """{"type":"AVDOED","fornavn":{"spoersmaal":"Fornavn","svar":"Overeksponert"},"etternavn":{"spoersmaal":"Etternavn","svar":"Mobiltelefon"},"foedselsnummer":{"spoersmaal":"Fødselsnummer / d-nummer","svar":"16498203950"},"foedselsdato":null,"datoForDoedsfallet":{"spoersmaal":"Når skjedde dødsfallet?","svar":{"innhold":"2023-11-20"}},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":{"innhold":"Norge"}},"utenlandsopphold":{"spoersmaal":"Har han eller hun bodd og/eller arbeidet i et annet land enn Norge etter fylte 16 år?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning": [{"land":{"spoersmaal":"Land","svar":{"innhold":"Sverige"}},"fraDato":{"spoersmaal":"Fra dato (valgfri)","svar":{"innhold":"2003-07-22"}},"tilDato":{"spoersmaal":"Til dato (valgfri)","svar":{"innhold":"2003-12-31"}},"oppholdsType":{"spoersmaal":"Bodd og/eller arbeidet?","svar": [{"verdi":"BODD","innhold":"Bodd"},{"verdi":"ARBEIDET","innhold":"Arbeidet"}]},"medlemFolketrygd":{"spoersmaal":"Var han eller hun medlem av folketrygden under oppholdet?","svar":{"verdi":"JA","innhold":"Ja"}},"pensjonsutbetaling":{"spoersmaal":"Oppgi eventuell pensjon han eller hun mottok fra dette landet (valgfri)","svar":{"innhold":"140 000"}}}]},"naeringsInntekt":{"spoersmaal":"Var han eller hun selvstendig næringsdrivende?","svar":{"verdi":"NEI","innhold":"Nei"}},"doedsaarsakSkyldesYrkesskadeEllerYrkessykdom":{"spoersmaal":"Skyldes dødsfallet yrkesskade eller yrkessykdom?","svar":{"verdi":"NEI","innhold":"Nei"}}}"""

        val avdoedUtenFdato = mapper.readValue(avdoedUtenFdatoJson, jacksonTypeRef<Person>())

        (avdoedUtenFdato is Avdoed) shouldBe true
        avdoedUtenFdato.foedselsdato shouldBe null
        avdoedUtenFdato.foedselsnummer?.svar shouldBe Foedselsnummer.of("16498203950")
    }

    @Test
    fun `Avdød uten fnr eller fødselsdato`() {
        val avdoedUtenFdatoJson = """{"type":"AVDOED","fornavn":{"spoersmaal":"Fornavn","svar":"Overeksponert"},"etternavn":{"spoersmaal":"Etternavn","svar":"Mobiltelefon"},"foedselsnummer":null,"foedselsdato":null,"datoForDoedsfallet":{"spoersmaal":"Når skjedde dødsfallet?","svar":{"innhold":"2023-11-20"}},"statsborgerskap":{"spoersmaal":"Statsborgerskap","svar":{"innhold":"Norge"}},"utenlandsopphold":{"spoersmaal":"Har han eller hun bodd og/eller arbeidet i et annet land enn Norge etter fylte 16 år?","svar":{"verdi":"JA","innhold":"Ja"},"opplysning": [{"land":{"spoersmaal":"Land","svar":{"innhold":"Sverige"}},"fraDato":{"spoersmaal":"Fra dato (valgfri)","svar":{"innhold":"2003-07-22"}},"tilDato":{"spoersmaal":"Til dato (valgfri)","svar":{"innhold":"2003-12-31"}},"oppholdsType":{"spoersmaal":"Bodd og/eller arbeidet?","svar": [{"verdi":"BODD","innhold":"Bodd"},{"verdi":"ARBEIDET","innhold":"Arbeidet"}]},"medlemFolketrygd":{"spoersmaal":"Var han eller hun medlem av folketrygden under oppholdet?","svar":{"verdi":"JA","innhold":"Ja"}},"pensjonsutbetaling":{"spoersmaal":"Oppgi eventuell pensjon han eller hun mottok fra dette landet (valgfri)","svar":{"innhold":"140 000"}}}]},"naeringsInntekt":{"spoersmaal":"Var han eller hun selvstendig næringsdrivende?","svar":{"verdi":"NEI","innhold":"Nei"}},"doedsaarsakSkyldesYrkesskadeEllerYrkessykdom":{"spoersmaal":"Skyldes dødsfallet yrkesskade eller yrkessykdom?","svar":{"verdi":"NEI","innhold":"Nei"}}}"""

        shouldThrow<Exception> {
            mapper.readValue(avdoedUtenFdatoJson, jacksonTypeRef<Person>())
        }
    }
}
