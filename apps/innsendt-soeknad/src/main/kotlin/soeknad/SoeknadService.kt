package no.nav.etterlatte.soeknad

import com.fasterxml.jackson.databind.JsonNode
import no.nav.etterlatte.libs.common.innsendtsoeknad.common.SoeknadRequest
import no.nav.etterlatte.libs.common.person.Foedselsnummer
import no.nav.etterlatte.toJson
import org.slf4j.LoggerFactory
import soeknad.LagretSoeknad
import soeknad.SoeknadID
import soeknad.SoeknadRepository
import soeknad.UlagretSoeknad

class SoeknadService(private val db: SoeknadRepository) {
    private val logger = LoggerFactory.getLogger(SoeknadService::class.java)

    fun sendSoeknad(innloggetBrukerFnr: Foedselsnummer, request: SoeknadRequest): Boolean {
        logger.info("Forsøker lagring av mottatte søknader (antall=${request.soeknader.size})")
        // Verifisere at det er innlogget bruker som er registrert som innsender
        val innsenderErInnlogget = request.soeknader.all { innloggetBrukerFnr == it.innsender.foedselsnummer.svar }
        if (!innsenderErInnlogget) {
            logger.error("Søknad innsender er ikke samme som innlogget bruker!")
            throw RuntimeException("Ugyldig innsender")
        }

        val ider = request.soeknader
            .map { UlagretSoeknad(it.soeker.foedselsnummer!!.svar.value, it.toJson(), it.type, it.kilde) }
            .map {
                logger.info("Ferdigstiller søknad (type=${it.type})")
                db.ferdigstillSoeknad(it)
            }

        return if (ider.size == request.soeknader.size) {
            logger.info("Lagret alle (${ider.size}) innsendte søknader.")
            true
        } else {
            logger.error("Kun ${ider.size} av ${request.soeknader.size} ble lagret.")
            false
        }
    }

    fun hentKladd(innloggetBruker: Foedselsnummer): LagretSoeknad? {
        return db.finnKladd(innloggetBruker.value)
            ?.also { logger.info("Fant kladd (id=${it.id})") }
    }

    fun lagreKladd(innloggetBruker: Foedselsnummer, soeknad: JsonNode): SoeknadID {
        val lagretkladd = db.lagreKladd(UlagretSoeknad(innloggetBruker.value, soeknad.toJson()))
            .also { logger.info("Lagret kladd (id=${it.id})") }

        return lagretkladd.id
    }

    fun slettKladd(innloggetBruker: Foedselsnummer) {
        db.slettKladd(innloggetBruker.value)
            ?.also { logger.info("Slettet kladd (id=${it})") }
    }
}
