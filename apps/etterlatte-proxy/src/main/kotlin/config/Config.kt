package no.nav.etterlatte.config

import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.server.config.ApplicationConfig
import no.nav.etterlatte.routes.httpClientWithProxy
import java.nio.file.Files
import java.nio.file.Paths

data class Config(
    val sts: Sts,
    val aad: AAD,
    val tilbakekrevingUrl: String,
    val simuleringOppdragUrl: String
) {
    data class Sts(
        val soapUrl: String,
        val serviceuser: ServiceUser
    ) {
        data class ServiceUser(
            val name: String,
            val password: String
        ) {
            override fun toString(): String = "name=$name, password=<REDACTED>"
        }
    }

    data class AAD(
        val metadata: Metadata,
        val clientId: String
    ) {
        data class Metadata(
            @JsonProperty("issuer") val issuer: String,
            @JsonProperty("jwks_uri") val jwksUri: String
        )
    }
}

suspend fun ApplicationConfig.load(): Config {
    val brukernavnOgPassordProvider = if (erLokalUtvikling()) {
        LokalUtvikling()
    } else {
        DevOgProd()
    }
    return Config(
        tilbakekrevingUrl = property("tilbakekreving.url").getString(),
        simuleringOppdragUrl = property("simuleringOppdrag.url").getString(),
        sts =
        Config.Sts(
            soapUrl = property("sts.soapUrl").getString(),
            serviceuser =
            Config.Sts.ServiceUser(
                name = brukernavnOgPassordProvider.navn(),
                password = brukernavnOgPassordProvider.passord(),
            )
        ),
        aad =
        Config.AAD(
            metadata = httpClientWithProxy().use { it.get(property("aad.wellKnownUrl").getString()).body() },
            clientId = property("aad.clientId").getString()
        )
    )
}

fun erLokalUtvikling() = System.getenv("NAIS_CLUSTER_NAME") !in listOf("dev-fss", "prod-fss")

interface BrukernavnOgPassordProvider {
    fun navn(): String
    fun passord(): String
}

class LokalUtvikling : BrukernavnOgPassordProvider {
    override fun navn() = "srvetterlatte"
    override fun passord() = "srv-password"
}

class DevOgProd : BrukernavnOgPassordProvider {
    override fun navn(): String = lesFil("/secrets/srvetterlatte/username")
    override fun passord(): String = lesFil("/secrets/srvetterlatte/password")

    private fun lesFil(sti: String) = Paths.get(sti).let { Files.readString(it) }
}