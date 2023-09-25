package no.nav.etterlatte.routes

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.typesafe.config.ConfigFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.coroutines.runBlocking
import no.nav.etterlatte.auth.clientCredential
import no.nav.etterlatte.config.Config
import org.slf4j.LoggerFactory
import java.util.*

fun Route.institusjonsoppholdRoute(config: Config) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    val inst2RouteSuffix = "/api/v1/person/institusjonsopphold/"
    val defaultConfig = ConfigFactory.load()
    val inst2Url = config.institusjonsoppholdUrl
    val httpKlient = getInstitusonsOppholdHttpklient(defaultConfig).also {
        if(environment?.developmentMode == false) {
            runBlocking {
                try {
                    it.get(inst2Url.plus("/api/ping")) {
                        header(HttpHeaders.NavConsumerId, "etterlatte-proxy")
                    }.let {
                        if (it.status == HttpStatusCode.OK) {
                            logger.info("Successfully pinged inst2-core")
                        } else {
                            logger.error("Couldnt not ping inst2-core, status: ${it.status}")
                        }
                    }
                } catch (e: ClientRequestException ) {
                    logger.error("Couldnt not ping inst2-core 4xx", e)
                } catch (e: RedirectResponseException) {
                    logger.error("Couldnt not ping inst2-core 3xx", e)
                } catch (e: ServerResponseException) {
                    logger.error("Couldnt not ping inst2-core 5xx", e)
                }
            }
        }
    }
    route("/inst2/{oppholdId}") {
        get {
            val callId = call.request.header(HttpHeaders.NavCallId) ?: UUID.randomUUID().toString()
            val oppholdId =
                call.parameters["oppholdId"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Mangler oppholdsid")
            try {
                val response = httpKlient.get(inst2Url.plus(inst2RouteSuffix.plus(oppholdId)).plus("?Med-Institusjonsinformasjon=true")) {
                    header(HttpHeaders.NavConsumerId, "etterlatte-proxy")
                    header(HttpHeaders.NavCallId, callId)
                }
                call.pipeResponse(response)

            } catch (cause: ResponseException) {
                logger.error("Feil i kall mot institusjonsopphold: ", cause)
                call.respond(HttpStatusCode.InternalServerError, cause.response)
            } catch (cause: Throwable) {
                logger.error("Feil i kall mot institusjonsopphold: ", cause)
                call.respondText(status = HttpStatusCode.InternalServerError) { cause.message ?: "Intern feil" }
            }
        }
    }
}

private fun getInstitusonsOppholdHttpklient(config: com.typesafe.config.Config): HttpClient {
    return httpClientClientCredentials(
        azureAppClientId = config.getString("aad.clientId"),
        azureAppJwk = config.getString("azure.app.jwk"),
        azureAppWellKnownUrl = config.getString("aad.wellKnownUrl"),
        azureAppScope = config.getString("institusjonsopphold.azure.scope")
    )
}

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

private val objectMapper: ObjectMapper = JsonMapper.builder()
    .addModule(JavaTimeModule())
    .addModule(KotlinModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
    .build()