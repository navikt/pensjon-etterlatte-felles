package no.nav.etterlatte

import java.util.UUID

data class SoeknadId(val soeknadId: String) {

    //TMS krever at varselId er en UUID, og vi trenger at det blir den samme UUID-en for samme søknad-ID
    val varselIdForEtterlatte: String
        get() = soeknadIdSomUuid().toString()

private fun soeknadIdSomUuid(): UUID {
    try {
        // Legger til et magisk tall for å skille oss fra andre varsel-produsenter
        val mostSigBits = 0x0DF6D91A_00000000
        val leastSigBits = soeknadId.toLong()
        return UUID(mostSigBits, leastSigBits)
    }
    catch (e: RuntimeException) {
        throw RuntimeException("Søknad-ID $soeknadId kan ikke representeres som en UUID", e)
    }
}

    override fun toString(): String {
        return soeknadId
    }
}
