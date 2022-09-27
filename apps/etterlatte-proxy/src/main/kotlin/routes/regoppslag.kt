package no.nav.etterlatte.routes

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.application.call
import io.ktor.client.features.ResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import no.nav.etterlatte.Config
import no.nav.etterlatte.StsClient
import no.nav.etterlatte.httpClient
import no.nav.etterlatte.pipeRequest
import no.nav.etterlatte.pipeResponse
import org.slf4j.LoggerFactory


fun Route.regoppslag(config: Config, stsClient: StsClient) {
    val logger = LoggerFactory.getLogger("no.pensjon.etterlatte")
    route("/regoppslag") {
        val httpClient = httpClient()
        val regoppslagUrl = config.regoppslag.url

        get("{ident}") {
            val stsToken = stsClient.getToken()

            try {
                val id = call.parameters["ident"]!!

                logger.info("AdresseRequest regular: ${AdresseRequest(id)}")
                logger.info("AdresseRequest jsonString: ${objectMapper.writeValueAsString(AdresseRequest(id))}")
                logger.info("Body: ${TextContent(objectMapper.writeValueAsString(AdresseRequest(id)), ContentType.Application.Json)}")

                val response = httpClient.post<HttpResponse>(regoppslagUrl + "/postadresse") {
                    header(HttpHeaders.Authorization, "Bearer $stsToken")
                    header("Nav_Callid", "barnepensjon")
                    // body = TextContent(objectMapper.writeValueAsString(AdresseRequest(id)), ContentType.Application.Json)
                    body = objectMapper.writeValueAsString(AdresseRequest(id))
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
    val tema: String = "PEN"
)

val objectMapper: ObjectMapper = JsonMapper.builder()
    .addModule(KotlinModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .disable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
    .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
    .build()