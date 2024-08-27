package no.nav.etterlatte

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueFactory
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.testing.testApplication
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ApplicationTest {
    private val mockOAuth2 = MockOAuth2Server()
    private lateinit var hoconApplicationConfig: HoconApplicationConfig

    @BeforeAll
    fun beforeAll() {
        mockOAuth2.start()
        hoconApplicationConfig =
            HoconApplicationConfig(
                ConfigFactory
                    .load()
                    .withValue(
                        "aad.wellKnownUrl",
                        ConfigValueFactory.fromAnyRef(mockOAuth2.wellKnownUrl("aad").toString())
                    )
            )
    }

    @AfterAll
    fun tearDown() {
        mockOAuth2.shutdown()
    }

    @Test
    fun `skal ikke autentisere for routes innenfor internal`() {
        testApplication {
            environment {
                config = hoconApplicationConfig
            }

            client.get("internal/is_alive").also {
                assertEquals(HttpStatusCode.OK, it.status)
            }
        }
    }

    @Test
    fun `skal returnere unauthorized dersom aad-token mangler for tilbakekreving-route`() {
        testApplication {
            environment {
                config = hoconApplicationConfig
            }

            client.post("aad/tilbakekreving/tilbakekrevingsvedtak").also {
                assertEquals(HttpStatusCode.Unauthorized, it.status)
            }
        }
    }
}