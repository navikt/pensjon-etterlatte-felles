package no.nav.etterlatte

import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.client.request.get
import io.ktor.config.ApplicationConfig

data class Config(
    val sts: Sts,
    val kodeverk: KODEVERK,
    val dok: DOK,
    val aad: AAD,
    val tokenX: TokenX,
    val inntektskomponenten: INNTEKTSKOMPONENTEN,
    val aareg: AAREG
) {
    data class DOK(
        val url: String
    )
    data class KODEVERK(
        val url: String
    )
    data class INNTEKTSKOMPONENTEN(
        val url: String
    )
    data class AAREG(
        val url: String
    )

    data class Sts(
        val url: String,
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

    data class TokenX(
        val metadata: Metadata,
        val clientId: String,
    ) {
        data class Metadata(
            @JsonProperty("issuer") val issuer: String,
            @JsonProperty("jwks_uri") val jwksUri: String,
        )
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
    dok = Config.DOK(url = property("dok.url").getString()),
    kodeverk = Config.KODEVERK(url = property("kodeverk.url").getString()),
    inntektskomponenten = Config.INNTEKTSKOMPONENTEN(url = property("inntektskomponenten.url").getString()),
    aareg = Config.AAREG(url = property("aareg.url").getString()),
    sts = Config.Sts(
        url = property("sts.url").getString(),
        serviceuser = Config.Sts.ServiceUser(
            name = property("serviceuser.name").getString(),
            password = property("serviceuser.password").getString(),
        )
    ),
    aad = Config.AAD(
        metadata = httpClientWithProxy().use{ it.get(property("aad.wellKnownUrl").getString())},
        clientId = property("aad.clientId").getString()
    ),
    tokenX = Config.TokenX(
        metadata = jsonClient().use{ it.get(property("tokenx.wellKnownUrl").getString())},
        clientId = property("tokenx.clientId").getString()
    )
)
