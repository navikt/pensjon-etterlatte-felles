package no.nav.etterlatte

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.application.ApplicationCall
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.request.receiveChannel
import io.ktor.response.respond
import io.ktor.util.filter
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.copyAndClose
import no.nav.etterlatte.security.ktor.clientCredential

fun defaultHttpClient() = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = JacksonSerializer {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}

fun tokenSecuredEndpoint() = HttpClient(CIO) {

    install(JsonFeature) {
        serializer = JacksonSerializer {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            registerModule(JavaTimeModule())
        }
    }
}

fun pdlhttpclient(aad: Config) = HttpClient(OkHttp) {
    val env = mutableMapOf(
        "AZURE_APP_CLIENT_ID" to aad.getString("client_id"),
        "AZURE_APP_WELL_KNOWN_URL" to aad.getString("well_known_url"),
        "AZURE_APP_OUTBOUND_SCOPE" to aad.getString("outbound"),
        "AZURE_APP_JWK" to aad.getString("client_jwk")
        )
    install(JsonFeature) { serializer = JacksonSerializer() }
    install(Auth) {
        clientCredential {
            config = env
        }
    }
}.also { Runtime.getRuntime().addShutdownHook(Thread { it.close() }) }

val proxiedContenHeaders = listOf(
    HttpHeaders.ContentType,
    HttpHeaders.ContentLength,
    HttpHeaders.TransferEncoding,
)
fun filterContenHeaders(requestHeaders: Headers): Headers{
    return Headers.build { appendAll(requestHeaders.filter { key, _ -> proxiedContenHeaders.any{it.equals(key, true)} }) }
}

class ProxiedContent(private val proxiedHeaders: Headers, private val content: ByteReadChannel, override val status: HttpStatusCode? = null): OutgoingContent.WriteChannelContent(){
    companion object{
        private val ignoredHeaders = listOf(HttpHeaders.ContentType, HttpHeaders.ContentLength, HttpHeaders.TransferEncoding, HttpHeaders.Authorization)
    }
    override val contentLength: Long? = proxiedHeaders[HttpHeaders.ContentLength]?.toLong()
    override val contentType: ContentType? = proxiedHeaders[HttpHeaders.ContentType]?.let { ContentType.parse(it) }
    override val headers: Headers = Headers.build {
        appendAll(proxiedHeaders.filter { key, _ ->
            ignoredHeaders.none { it.equals(key, ignoreCase = true) }
        })
    }
    override suspend fun writeTo(channel: ByteWriteChannel) {
        content.copyAndClose(channel)
    }
}

suspend fun HttpRequestBuilder.pipeRequest(call: ApplicationCall, customHeaders: List<String> = emptyList()){
    val requestHeadersToProxy = listOf(
        HttpHeaders.Accept,
        HttpHeaders.AcceptCharset,
        HttpHeaders.AcceptEncoding,
    ) + customHeaders
    headers.appendAll(call.request.headers.filter { key, _ ->
        requestHeadersToProxy.any{it.equals(key, true)}
    })
    body = ProxiedContent(filterContenHeaders(call.request.headers), call.receiveChannel())
}

suspend fun ApplicationCall.pipeResponse(response: HttpResponse) {
    respond(ProxiedContent(response.headers, if(response.content.isClosedForRead) { response.receive() } else { response.content }, response.status))
}

val NavCallId: String
    get() = "Nav-Call-Id"

