package no.nav.etterlatte.config

import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.server.config.ApplicationConfig
import no.nav.etterlatte.routes.httpClientWithProxy

data class Config(
    val sts: Sts,
    val aad: AAD,
    val institusjonsoppholdUrl: String,
    val tilbakekrevingUrl: String
) {

    data class Sts(
        val restUrl: String,
        val soapUrl: String,
        val serviceuser: ServiceUser,
    ) {
        data class ServiceUser(
            val name: String,
            val password: String,
        ) {
            override fun toString(): String {
                return "name=$name, password=<REDACTED>"
            }
        }
    }

    data class AAD(
        val metadata: Metadata,
        val clientId: String,
    ) {
        data class Metadata(
            @JsonProperty("issuer") val issuer: String,
            @JsonProperty("jwks_uri") val jwksUri: String,
        )
    }
}

suspend fun ApplicationConfig.load() = Config(
    institusjonsoppholdUrl = property("institusjonsopphold.url").getString(),
    tilbakekrevingUrl = property("tilbakekreving.url").getString(),
    sts = Config.Sts(
        restUrl = property("sts.restUrl").getString(),
        soapUrl = property("sts.soapUrl").getString(),
        serviceuser = Config.Sts.ServiceUser(
            name = property("serviceuser.name").getString(),
            password = property("serviceuser.password").getString(),
        )
    ),
    aad = Config.AAD(
        metadata = httpClientWithProxy().use { it.get(property("aad.wellKnownUrl").getString()).body() },
        clientId = property("aad.clientId").getString()
    )
)
