package no.nav.etterlatte

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import no.nav.helse.rapids_rivers.JsonMessage
import org.slf4j.MDC
import java.util.*

class Journalfoer(private val client: HttpClient, private val apiUrl: String) : JournalfoerDok {

    override suspend fun journalfoerDok(dokumentInnhold: JsonMessage, pdf: ByteArray): String {
        val apiUrl = "hvorfinnerjegDokarkivet"

        val objectMapper = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        val jorp: JournalpostInfo? = objectMapper.treeToValue(dokumentInnhold["@journalpostInfo"])


        return jorp?.let {

            val dok: List<JournalpostDokument> = listOf(
                JournalpostDokument(
                    tittel = jorp.tittel ,
                    dokumentKategori = DokumentKategori.IB,
                    dokumentvarianter = listOf(
                        DokumentVariant.ArkivPDF(fysiskDokument = Base64.getEncoder().encodeToString(pdf)),
                        DokumentVariant.OriginalJson(fysiskDokument = dokumentInnhold["@skjema_info"].asText()
                        )
                    )
                ),
            )


            client.post<String>(apiUrl) {
                listOf("forsoekFerdigstill" to "true")
                header("Content-Type", "application/json")
                header("Accept", "application/json")
                header("X-Correlation-ID", MDC.get("X-Correlation-ID") ?: UUID.randomUUID().toString())
                body =

                    JournalpostRequest(
                        tittel = jorp.tittel,
                        journalpostType = JournalPostType.INNGAAENDE,
                        tema = "PEN",
                        kanal = "NAV_NO",
                        //behandlingstema = dokumentInnhold["@behandlingstema"].asText(),
                        //journalfoerendeEnhet = dokumentInnhold["@journalfoerendeEnhet"].asText(),

                        avsenderMottaker = AvsenderMottaker(
                            id = jorp.avsenderMottaker.id,
                            navn = jorp.avsenderMottaker.navn,
                            idType = jorp.avsenderMottaker.idType
                        ),
                        bruker = Bruker(
                            id = jorp.bruker.id,
                            idType = "FNR"
                        ),

                        /*   sak = Fagsak(
                               fagsakId = "id",
                               fagsaksystem = "BP",
                               sakstype = "soknad"
                               ),
                         */

                        dokumenter = dok
                    )
            }
        }
            ?: "klarte ikke å journalføre"
    }
}

data class JournalpostInfo(
    val tittel: String,
    val avsenderMottaker: AvsenderMottaker,
    val bruker: Bruker,
)

internal data class JournalpostRequest(
    val tittel: String,
    val journalpostType: JournalPostType,
    val tema: String,
    val kanal: String?,
    //val behandlingstema: String,
    //val journalfoerendeEnhet: String,
    val avsenderMottaker: AvsenderMottaker,
    val bruker: Bruker,
    //val sak: Fagsak?,
    var dokumenter: List<JournalpostDokument>
)

data class AvsenderMottaker(
    val id: String,
    val idType: String = "FNR",
    val navn: String
)

data class Bruker(
    val id: String,
    val idType: String = "FNR"
)

data class Fagsak(
    val fagsakId: String,
    val fagsaksystem: String = "SUPSTONAD",
    val sakstype: String = "FAGSAK"
)

data class JournalpostDokument(
    val tittel: String,
    val dokumentKategori: DokumentKategori,
    val brevkode: String = "XX.YY-ZZ",
    val dokumentvarianter: List<DokumentVariant>
)

sealed class DokumentVariant {
    abstract val filtype: String
    abstract val fysiskDokument: String
    abstract val variantformat: String

    data class ArkivPDF(
        override val fysiskDokument: String,
    ) : DokumentVariant() {
        override val filtype: String = "PDFA"
        override val variantformat: String = "ARKIV"
    }

    data class OriginalJson(
        override val fysiskDokument: String,
    ) : DokumentVariant() {
        override val filtype: String = "JSON"
        override val variantformat: String = "ORIGINAL"
    }
}

enum class JournalPostType(val type: String) {
    INNGAAENDE("INNGAAENDE"),
    UTGAAENDE("UTGAAENDE")
}

enum class DokumentKategori(val type: String) {
    SOK("SOK"),
    VB("VB"),
    IB("IB")
}

