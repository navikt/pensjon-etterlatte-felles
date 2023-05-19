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

    val mockOAuth2 = MockOAuth2Server()
    private lateinit var hoconApplicationConfig: HoconApplicationConfig

    @BeforeAll
    fun beforeAll() {
        mockOAuth2.start()
        hoconApplicationConfig = HoconApplicationConfig(ConfigFactory.load()
            .withValue("tokenx.wellKnownUrl", ConfigValueFactory.fromAnyRef(mockOAuth2.wellKnownUrl("tokenx").toString()))
            .withValue("aad.wellKnownUrl", ConfigValueFactory.fromAnyRef(mockOAuth2.wellKnownUrl("aad").toString()))
        )

    }
    @AfterAll
    fun tearDown(){
        mockOAuth2.shutdown()
    }

    @Test
    fun testRoot() {
        testApplication {
            environment {
                config = hoconApplicationConfig
            }

            client.get("internal/is_alive").also {
                assertEquals(HttpStatusCode.OK, it.status)
                assertEquals("Alive", it.body<String>())
            }
        }
    }

    @Test
    fun testAareg() {
        testApplication {
            environment {
                config = hoconApplicationConfig
            }

            client.get("aad/aareg/arbeidstaker/arbeidsforhold").also {
                assertEquals(HttpStatusCode.Unauthorized, it.status)
            }
        }
    }

    @Test
    fun testInntektskomponenten() {
        testApplication {
            environment {
                config = hoconApplicationConfig
            }

            client.post("aad/inntektskomponenten").also {
                assertEquals(HttpStatusCode.Unauthorized, it.status)
            }
        }
    }
}
