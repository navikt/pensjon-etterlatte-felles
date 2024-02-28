package no.nav.etterlatte

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Soeknad(
    val innsender: Innsender,
    val type: Type
) {
    enum class Type {
        GJENLEVENDEPENSJON,
        BARNEPENSJON,
        OMSTILLINGSSTOENAD
    }
}

data class Innsender(
    val foedselsnummer: Foedselsnummer
)

data class Foedselsnummer(
    @JsonProperty("svar")
    val value: String
)
