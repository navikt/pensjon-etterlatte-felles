package no.nav.etterlatte

import no.nav.etterlatte.omsMeldInnEndring.OmsMeldInnEndringNotifikasjon
import no.nav.etterlatte.soeknad.SoeknadNotifikasjon
import no.nav.helse.rapids_rivers.RapidApplication

fun main() {
    val env =
        System.getenv().toMutableMap().apply {
            put("KAFKA_CONSUMER_GROUP_ID", get("NAIS_APP_NAME")!!.replace("-", ""))
        }
    val sendNotifikasjonApp = SendNotifikasjon(env)
    RapidApplication
        .create(env)
        .also {
            SoeknadNotifikasjon(sendNotifikasjonApp, it)
            OmsMeldInnEndringNotifikasjon(sendNotifikasjonApp, it)
        }.start()
}