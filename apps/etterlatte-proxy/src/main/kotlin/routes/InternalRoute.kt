package no.nav.etterlatte.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

fun Route.internalRoute() {
    route("/internal") {
        get("/is_alive") {
            call.respond(HttpStatusCode.OK)
        }
        get("/is_ready") {
            call.respond(HttpStatusCode.OK)
        }
        get("/metrics") {
            call.respond(PrometheusMeterRegistry(PrometheusConfig.DEFAULT))
        }
    }
}