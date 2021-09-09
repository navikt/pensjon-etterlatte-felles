package no.nav.etterlatte

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import no.nav.etterlatte.pdlAdressebeskyttelse.PdlAdressebeskyttelse
import no.nav.etterlatte.security.ktor.clientCredential

class AppBuilder(private val props: Map<String, String>) {
    companion object {
        const val CONFIG_PDL_URL = "PDL_URL"
    }

    fun pdlService() = PdlAdressebeskyttelse(pdlhttpclient(), props[CONFIG_PDL_URL]!!)

    private fun pdlhttpclient() = HttpClient(OkHttp) {
        install(JsonFeature) { serializer = JacksonSerializer() }
        install(Auth) {
            clientCredential {
                config = props.toMutableMap()
                    .apply { put("AZURE_APP_OUTBOUND_SCOPE", "api://dev-gcp.etterlatte.etterlatte-pdl-proxy/pdl") }
            }
        }
    }.also { Runtime.getRuntime().addShutdownHook(Thread { it.close() }) }


}

