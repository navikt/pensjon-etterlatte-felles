package no.nav.etterlatte

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import no.nav.etterlatte.routes.simuleringOppdragRoute
import no.nav.etterlatte.routes.tilbakekrevingRoute
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagAnnulerRequest
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagAnnulerResponse
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentDetaljRequest
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentDetaljResponse
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentListeRequest
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentListeResponse
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingPortType
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingsvedtakRequest
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingsvedtakResponse
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SimulerBeregningFeilUnderBehandling
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SimulerFpService
import no.nav.system.os.entiteter.beregningskjema.Beregning
import no.nav.system.os.entiteter.beregningskjema.BeregningStoppnivaa
import no.nav.system.os.entiteter.beregningskjema.BeregningStoppnivaaDetaljer
import no.nav.system.os.entiteter.beregningskjema.BeregningsPeriode
import no.nav.system.os.tjenester.simulerfpservice.feil.FeilUnderBehandling
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SendInnOppdragRequest
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SendInnOppdragResponse
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningRequest
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningResponse
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagBelopDto
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagDto
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagPeriodeDto
import no.nav.tilbakekreving.typer.v1.PeriodeDto
import no.nav.tilbakekreving.typer.v1.TypeGjelderDto
import no.nav.tilbakekreving.typer.v1.TypeKlasseDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tools.jackson.module.kotlin.jacksonObjectMapper
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import javax.xml.datatype.DatatypeFactory
import no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.SimulerBeregningResponse as SimuleringResultat

/**
 * Kontrakt mot konsumentene (etterlatte-tilbakekreving og etterlatte-utbetaling) for JSON-formatet til proxyen.
 */
internal class ProxyJsonKontraktTest {
    private val mapper = jacksonObjectMapper()

    private var mottattVedtak: TilbakekrevingsvedtakRequest? = null
    private var mottattSimulering: SimulerBeregningRequest? = null
    private var simuleringFeiler = false

    @Test
    fun `tilbakekrevingsvedtak leser datoer som UTC og skriver dem som epoch millis`() =
        testProxy { client ->
            val response = client.postJson(
                "/tilbakekreving/tilbakekrevingsvedtak",
                """
                {
                  "tilbakekrevingsvedtak": {
                    "vedtakId": 42,
                    "datoVedtakFagsystem": "2024-05-02",
                    "tilbakekrevingsperiode": [
                      {
                        "periode": {
                          "fom": "2024-01-01",
                          "tom": "2024-01-31"
                        },
                        "tilbakekrevingsbelop": [
                          {
                            "kodeKlasse": "BPGJENLEV",
                            "belopTilbakekreves": 10000.00
                          }
                        ]
                      }
                    ]
                  }
                }
                """.trimIndent()
            )

            assertEquals(HttpStatusCode.OK, response.status)

            val vedtak = mottattVedtak!!.tilbakekrevingsvedtak
            assertEquals(BigInteger.valueOf(42), vedtak.vedtakId)
            assertEquals(0, vedtak.datoVedtakFagsystem.timezone)
            assertEquals(Instant.parse("2024-05-02T00:00:00Z"), vedtak.datoVedtakFagsystem.toGregorianCalendar().toInstant())

            val periode = vedtak.tilbakekrevingsperiode.single()
            assertEquals(
                Instant.parse("2024-01-31T00:00:00Z"),
                periode.periode.tom
                    .toGregorianCalendar()
                    .toInstant()
            )
            assertEquals(BigDecimal("10000.00"), periode.tilbakekrevingsbelop.single().belopTilbakekreves)

            val dato = mapper.readTree(response.bodyAsText())["tilbakekrevingsvedtak"]["datoVedtakFagsystem"]
            assertTrue(dato.isIntegralNumber)
            assertEquals(Instant.parse("2024-05-02T00:00:00Z").toEpochMilli(), dato.longValue())
        }

    @Test
    fun `kravgrunnlag skriver datoer som epoch millis og enums som navn`() =
        testProxy { client ->
            val response =
                client.postJson(
                    "/tilbakekreving/kravgrunnlag",
                    """
                        {
                          "hentkravgrunnlag": {
                            "kodeAksjon": "3",
                            "kravgrunnlagId": 123456789
                          }
                        }
                    """.trimIndent()
                )

            assertEquals(HttpStatusCode.OK, response.status)

            val kravgrunnlag = mapper.readTree(response.bodyAsText())["detaljertkravgrunnlag"]
            assertEquals(123456789L, kravgrunnlag["kravgrunnlagId"].longValue())
            assertTrue(kravgrunnlag["datoVedtakFagsystem"].isIntegralNumber)
            assertEquals(
                xmlDato("2024-05-02").toGregorianCalendar().timeInMillis,
                kravgrunnlag["datoVedtakFagsystem"].longValue()
            )
            assertEquals("PERSON", kravgrunnlag["typeGjelderId"].asString())

            val belop = kravgrunnlag["tilbakekrevingsPeriode"][0]["tilbakekrevingsBelop"][0]
            assertEquals("YTEL", belop["typeKlasse"].asString())
            assertEquals(10000.0, belop["belopTilbakekreves"].doubleValue())
        }

    @Test
    fun `simulering leser nestede lister og skriver boolean felter uten is-prefiks`() =
        testProxy { client ->
            val response =
                client.postJson(
                    "/simuleringoppdrag/simulerberegning",
                    """
                    {
                      "request": {
                        "oppdrag": {
                          "kodeFagomraade": "BARNEPE",
                          "enhet": [
                            {
                              "typeEnhet": "BOS",
                              "enhet": "4819"
                            }
                          ],
                          "oppdragslinje": [
                            {
                              "delytelseId": "1",
                              "sats": 1234.00,
                              "grad": [
                                {
                                  "typeGrad": "UFOR",
                                  "grad": 100
                                }
                              ],
                              "attestant": [
                                {
                                  "attestantId": "Z123456"
                                }
                              ]
                            }
                          ]
                        },
                        "simuleringsPeriode": {
                          "datoSimulerFom": "2024-01-01",
                          "datoSimulerTom": "2024-01-31"
                        }
                      }
                    }
                    """.trimIndent()
                )

            assertEquals(HttpStatusCode.OK, response.status)

            val request = mottattSimulering!!.request
            assertEquals("4819", request.oppdrag.enhet.single().enhet)

            val linje = request.oppdrag.oppdragslinje.single()
            assertEquals(BigDecimal("1234.00"), linje.sats)
            assertEquals(BigInteger.valueOf(100), linje.grad.single().grad)
            assertEquals("Z123456", linje.attestant.single().attestantId)
            assertEquals("2024-01-31", request.simuleringsPeriode.datoSimulerTom)

            val stoppnivaa =
                mapper.readTree(
                    response.bodyAsText()
                )["response"]["simulering"]["beregningsPeriode"][0]["beregningStoppnivaa"][0]
            assertFalse(stoppnivaa["feilkonto"].booleanValue())
            assertTrue(stoppnivaa["beregningStoppnivaaDetaljer"][0]["tilbakeforing"].booleanValue())
        }

    @Test
    fun `simulering returnerer faultInfo som JSON ved feil fra oppdrag`() =
        testProxy { client ->
            simuleringFeiler = true

            val response =
                client.postJson(
                    "/simuleringoppdrag/simulerberegning",
                    """
                        {
                          "request": {
                            "oppdrag": {
                              "fagsystemId": "999"
                            }
                          }
                        }""".trimIndent()
                )

            assertEquals(HttpStatusCode.InternalServerError, response.status)
            val feil = mapper.readTree(response.bodyAsText())
            assertEquals("Noe gikk galt", feil["errorMessage"].asString())
            assertEquals("TEKNISK", feil["errorType"].asString())
        }

    private fun testProxy(block: suspend (HttpClient) -> Unit) =
        testApplication {
            application {
                installContentNegotiation()
                routing {
                    tilbakekrevingRoute(tilbakekreving)
                    simuleringOppdragRoute(simulering)
                }
            }
            block(client)
        }

    private suspend fun HttpClient.postJson(path: String, body: String) = post(path) {
        contentType(ContentType.Application.Json)
        setBody(body)
    }

    private fun xmlDato(dato: String) = DatatypeFactory.newInstance().newXMLGregorianCalendar(dato)

    private val tilbakekreving =
        object : TilbakekrevingPortType {
            override fun tilbakekrevingsvedtak(request: TilbakekrevingsvedtakRequest) =
                TilbakekrevingsvedtakResponse().apply {
                    mottattVedtak = request
                    tilbakekrevingsvedtak = request.tilbakekrevingsvedtak
                }

            override fun kravgrunnlagHentDetalj(request: KravgrunnlagHentDetaljRequest) =
                KravgrunnlagHentDetaljResponse().apply {
                    detaljertkravgrunnlag =
                        DetaljertKravgrunnlagDto().apply {
                            kravgrunnlagId = request.hentkravgrunnlag.kravgrunnlagId
                            datoVedtakFagsystem = xmlDato("2024-05-02")
                            typeGjelderId = TypeGjelderDto.PERSON
                            tilbakekrevingsPeriode.add(
                                DetaljertKravgrunnlagPeriodeDto().apply {
                                    periode =
                                        PeriodeDto().apply {
                                            fom = xmlDato("2024-01-01")
                                            tom = xmlDato("2024-01-31")
                                        }
                                    tilbakekrevingsBelop.add(
                                        DetaljertKravgrunnlagBelopDto().apply {
                                            typeKlasse = TypeKlasseDto.YTEL
                                            belopTilbakekreves = BigDecimal("10000.00")
                                        }
                                    )
                                }
                            )
                        }
                }

            override fun kravgrunnlagHentListe(request: KravgrunnlagHentListeRequest): KravgrunnlagHentListeResponse =
                KravgrunnlagHentListeResponse()

            override fun kravgrunnlagAnnuler(request: KravgrunnlagAnnulerRequest): KravgrunnlagAnnulerResponse =
                KravgrunnlagAnnulerResponse()
        }

    private val simulering =
        object : SimulerFpService {
            override fun simulerBeregning(request: SimulerBeregningRequest): SimulerBeregningResponse {
                mottattSimulering = request
                if (simuleringFeiler) {
                    throw SimulerBeregningFeilUnderBehandling(
                        "feil",
                        FeilUnderBehandling().apply {
                            errorMessage = "Noe gikk galt"
                            errorType = "TEKNISK"
                        }
                    )
                }
                return SimulerBeregningResponse().apply {
                    response =
                        SimuleringResultat().apply {
                            simulering =
                                Beregning().apply {
                                    beregningsPeriode.add(
                                        BeregningsPeriode().apply {
                                            beregningStoppnivaa.add(
                                                BeregningStoppnivaa().apply {
                                                    isFeilkonto = false
                                                    beregningStoppnivaaDetaljer.add(
                                                        BeregningStoppnivaaDetaljer().apply { isTilbakeforing = true }
                                                    )
                                                }
                                            )
                                        }
                                    )
                                }
                        }
                }
            }

            override fun sendInnOppdrag(request: SendInnOppdragRequest): SendInnOppdragResponse =
                SendInnOppdragResponse()
        }
}