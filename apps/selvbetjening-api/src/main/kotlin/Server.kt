package no.nav.etterlatte

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.config.HoconApplicationConfig
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.withContext
import no.nav.etterlatte.health.healthApi
import no.nav.etterlatte.person.PersonClient
import no.nav.etterlatte.person.personApi
import no.nav.etterlatte.soknad.soknadApi
import no.nav.security.token.support.ktor.TokenValidationContextPrincipal
import no.nav.security.token.support.ktor.tokenValidationSupport

class Server(val applicationContext: ApplicationContext) {
    val configuration = HoconApplicationConfig(applicationContext.config)
    val personClient = PersonClient(applicationContext.httpClient())
    val engine = embeddedServer(io.ktor.server.cio.CIO, environment = applicationEngineEnvironment {
        module {

            install(ContentNegotiation) {
                jackson()
            }

            install(Authentication) {
                tokenValidationSupport(config = configuration)
            }
            install(Locations)


            routing {
                healthApi()
                personApi(personClient)
                soknadApi()

                route("api") {
                    get {
                        call.respond(HttpStatusCode.OK, PdlMock().personInfo(""))
                    }
                }
                authenticate {
                    route("secure") {
                        get {
                            withSecurityCOntext(applicationContext) {
                                applicationContext.pdl.personInfo(applicationContext.securityContext.get().user()!!)
                            }.also {
                                call.respond(
                                    HttpStatusCode.OK, it
                                )
                            }
                        }
                    }
                }
            }
        }

        connector {
            port = 8080
        }
    }
    )


    fun run() = engine.start(true)
}

suspend fun <T> PipelineContext<*, ApplicationCall>.withSecurityCOntext(
    ctx: ApplicationContext,
    block: suspend CoroutineScope.() -> T
): T {
    return withContext(
        Dispatchers.Default + ctx.securityContext.asContextElement(
            value = SecurityContext(
                call.principal<TokenValidationContextPrincipal>()?.context!!
            )
        ), block
    )
}