package no.nav.etterlatte.dokarkiv

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

data class DokarkivDokument(
    val dokumentInfoId: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DokarkivResponse(
    val journalpostId: String,
    val journalpoststatus: String? = null,
    val melding: String? = null,
    val journalpostferdigstilt: Boolean,
    val dokumenter: List<DokarkivDokument> = emptyList()
)
