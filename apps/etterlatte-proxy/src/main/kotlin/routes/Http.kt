package no.nav.etterlatte.routes

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveChannel
import io.ktor.server.response.respond
import io.ktor.util.InternalAPI
import io.ktor.util.filter
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.copyAndClose
import org.apache.http.impl.conn.SystemDefaultRoutePlanner
import java.net.ProxySelector

fun httpClient() =
    HttpClient(Apache) {
        install(Logging) {
            level = LogLevel.INFO
        }
    }.also { Runtime.getRuntime().addShutdownHook(Thread { it.close() }) }

fun httpClientWithProxy() =
    HttpClient(Apache) {
        install(ContentNegotiation) {
            jackson { configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) }
        }
        engine {
            customizeClient {
                setRoutePlanner(SystemDefaultRoutePlanner(ProxySelector.getDefault()))
            }
        }
    }

val proxiedContenHeaders =
    listOf(
        HttpHeaders.ContentType,
        HttpHeaders.ContentLength,
        HttpHeaders.TransferEncoding
    )

fun filterContenHeaders(requestHeaders: Headers): Headers =
    Headers.build {
        appendAll(requestHeaders.filter { key, _ -> proxiedContenHeaders.any { it.equals(key, true) } })
    }

class ProxiedContent(
    private val proxiedHeaders: Headers,
    private val content: ByteReadChannel,
    override val status: HttpStatusCode? = null
) : OutgoingContent.WriteChannelContent() {
    companion object {
        private val ignoredHeaders =
            listOf(
                HttpHeaders.ContentType,
                HttpHeaders.ContentLength,
                HttpHeaders.TransferEncoding,
                HttpHeaders.Authorization
            )
    }

    override val contentLength: Long? = proxiedHeaders[HttpHeaders.ContentLength]?.toLong()
    override val contentType: ContentType? = proxiedHeaders[HttpHeaders.ContentType]?.let { ContentType.parse(it) }
    override val headers: Headers =
        Headers.build {
            appendAll(
                proxiedHeaders.filter { key, _ ->
                    ignoredHeaders.none { it.equals(key, ignoreCase = true) }
                }
            )
        }

    override suspend fun writeTo(channel: ByteWriteChannel) {
        content.copyAndClose(channel)
    }
}

suspend fun HttpRequestBuilder.pipeRequest(
    call: ApplicationCall,
    customHeaders: List<String> = emptyList()
) {
    val requestHeadersToProxy =
        listOf(
            HttpHeaders.Accept,
            HttpHeaders.AcceptCharset,
            HttpHeaders.AcceptEncoding
        ) + customHeaders
    headers.appendAll(
        call.request.headers.filter { key, _ ->
            requestHeadersToProxy.any { it.equals(key, true) }
        }
    )
    setBody(ProxiedContent(filterContenHeaders(call.request.headers), call.receiveChannel()))
}

@OptIn(InternalAPI::class)
suspend fun ApplicationCall.pipeResponse(response: HttpResponse) {
    respond(
        ProxiedContent(
            response.headers,
            if (response.content.isClosedForRead) {
                response.body()
            } else {
                response.content
            },
            response.status
        )
    )
}

val HttpHeaders.NavCallId: String
    get() = "Nav-Call-Id"
val HttpHeaders.NavConsumerId: String
    get() = "Nav-Consumer-Id"
val HttpHeaders.NavPersonident: String
    get() = "Nav-Personident"
val HttpHeaders.NavConsumerToken: String
    get() = "Nav-Consumer-Token"