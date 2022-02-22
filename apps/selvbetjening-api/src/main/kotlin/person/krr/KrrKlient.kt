package no.nav.etterlatte.person.krr

import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import no.nav.etterlatte.libs.common.person.Foedselsnummer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

interface Krr {
    suspend fun hentDigitalKontaktinformasjon(fnr: Foedselsnummer): DigitalKontaktinformasjon?
}

class KrrKlient(private val client: HttpClient) : Krr {
    private val logger: Logger = LoggerFactory.getLogger(KrrKlient::class.java)

    override suspend fun hentDigitalKontaktinformasjon(fnr: Foedselsnummer): DigitalKontaktinformasjon? {
        logger.info("Henter kontaktopplysninger fra KRR.")

        return try {
            client.get("person") {
                header(HttpHeaders.NavPersonIdent, fnr.value)
                header(HttpHeaders.NavCallId, UUID.randomUUID().toString())
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        } catch (cause: Throwable) {
            logger.warn("Klarte ikke å hente kontaktinformasjon fra KRR.", KrrException(cause))
            return null
        }
    }
}

class KrrException(cause: Throwable) :
    Exception("Klarte ikke å hente digital kontaktinfo fra Krr", cause)

val HttpHeaders.NavCallId: String
    get() = "Nav-Call-Id"
val HttpHeaders.NavPersonIdent: String
    get() = "Nav-Personident"