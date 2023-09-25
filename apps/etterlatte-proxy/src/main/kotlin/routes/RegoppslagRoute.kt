package no.nav.etterlatte.routes

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import no.nav.etterlatte.config.Config
import no.nav.etterlatte.auth.sts.StsRestClient
import org.slf4j.LoggerFactory
import java.util.UUID


fun Route.regoppslagRoute(config: Config, stsClient: StsRestClient) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    route("/regoppslag") {
        val httpClient = httpClient()
        val regoppslagUrl = config.regoppslagUrl

        get("{ident}") {
            val stsToken = stsClient.getToken()

            try {
                val id = call.parameters["ident"]!!
                val callId = call.request.header(HttpHeaders.NavCallId) ?: UUID.randomUUID().toString()

                val response = httpClient.post("$regoppslagUrl/postadresse") {
                    header(HttpHeaders.Authorization, "Bearer $stsToken")
                    header("Nav_Callid", callId)
                    setBody(objectMapper.writeValueAsString(AdresseRequest(id)))
                    contentType(ContentType.Application.Json)
                }
                call.pipeResponse(response)
            } catch (cause: ResponseException) {
                logger.error("Feil i kall mot Regoppslag: ", cause)
                call.pipeResponse(cause.response)
            } catch (cause: Throwable) {
                logger.error("Feil i kall mot Regoppslag: ", cause)
                call.respondText(status = HttpStatusCode.InternalServerError) { cause.message ?: "Intern feil" }
            }
        }
    }
}

data class AdresseRequest(
    val ident: String,
    val tema: String = "PEN" // Todo: mulig bytte tema til et av de nye
)

private val objectMapper: ObjectMapper = JsonMapper.builder()
    .addModule(KotlinModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .disable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
    .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
    .build()