package no.nav.etterlatte

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.jackson.JacksonConverter
import java.util.*

fun httpClientClientCredentials(
    azureAppClientId: String,
    azureAppJwk: String,
    azureAppWellKnownUrl: String,
    azureAppScope: String,
    ekstraJacksoninnstillinger: ((o: ObjectMapper) -> Unit) = { }
) = HttpClient(OkHttp) {
    expectSuccess = true
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(objectMapper))
        ekstraJacksoninnstillinger(objectMapper)
    }
    install(Auth) {
        clientCredential {
            config = mapOf(
                "AZURE_APP_CLIENT_ID" to azureAppClientId,
                "AZURE_APP_JWK" to azureAppJwk,
                "AZURE_APP_WELL_KNOWN_URL" to azureAppWellKnownUrl,
                "AZURE_APP_OUTBOUND_SCOPE" to azureAppScope
            )
        }
    }
    defaultRequest {
        header(HttpHeaders.XCorrelationId, UUID.randomUUID().toString())
    }
}.also { Runtime.getRuntime().addShutdownHook(Thread { it.close() }) }

val objectMapper: ObjectMapper = JsonMapper.builder()
    .addModule(JavaTimeModule())
    .addModule(KotlinModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
    .build()
