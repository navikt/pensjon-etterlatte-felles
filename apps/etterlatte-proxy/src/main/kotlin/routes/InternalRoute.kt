package no.nav.etterlatte.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.micrometer.core.instrument.Clock
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.prometheus.metrics.model.registry.PrometheusRegistry

object Metrikker {
    private val collectorRegistry = PrometheusRegistry.defaultRegistry

    val registry =
        PrometheusMeterRegistry(
            PrometheusConfig.DEFAULT,
            collectorRegistry,
            Clock.SYSTEM,
        )
}

fun Route.internalRoute() {
    route("/internal") {
        get("/is_alive") {
            call.respond(HttpStatusCode.OK)
        }
        get("/is_ready") {
            call.respond(HttpStatusCode.OK)
        }
        get("/metrics") {
            call.respond(Metrikker.registry)
        }
    }
}