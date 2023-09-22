package no.nav.etterlatte.config

import no.nav.etterlatte.auth.sts.ServiceUserConfig
import no.nav.etterlatte.auth.sts.wrapInStsClient
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingPortType
import org.apache.cxf.ext.logging.LoggingFeature
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.slf4j.LoggerFactory
import javax.xml.namespace.QName

class TilbakekrevingConfig(config: Config, private val enableLogging: Boolean = false) {

    private val tilbakekrevingUrl = config.tilbakekrevingUrl
    private val sts = config.sts

    private val logger = LoggerFactory.getLogger(javaClass)

    fun createTilbakekrevingService(): TilbakekrevingPortType {
        logger.info("Bruker tilbakekrevingService url $tilbakekrevingUrl")

        return JaxWsProxyFactoryBean().apply {
            address = tilbakekrevingUrl
            wsdlURL = WSDL
            serviceName = SERVICE
            endpointName = PORT
            serviceClass = TilbakekrevingPortType::class.java
            if (enableLogging) features = listOf(
                LoggingFeature().apply {
                    setVerbose(true)
                    setPrettyLogging(true)
                }
            )
        }.wrapInStsClient(sts.soapUrl, ServiceUserConfig(sts.serviceuser.name, sts.serviceuser.password), true)
    }

    private companion object {
        private const val WSDL = "wsdl/no/nav/tilbakekreving/tilbakekreving-v1-tjenestespesifikasjon.wsdl"
        private const val NAMESPACE = "http://okonomi.nav.no/tilbakekrevingService/"
        private val SERVICE = QName(NAMESPACE, "TilbakekrevingService")
        private val PORT = QName(NAMESPACE, "TilbakekrevingServicePort")
    }
}
