package no.nav.etterlatte.config

import no.nav.etterlatte.auth.sts.ServiceUserConfig
import no.nav.etterlatte.auth.sts.wrapInStsClient
import no.nav.system.os.eksponering.simulerfpservicewsbinding.SimulerFpService
import org.apache.cxf.ext.logging.LoggingFeature
import org.apache.cxf.feature.Feature
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.ws.addressing.WSAddressingFeature
import org.slf4j.LoggerFactory
import javax.xml.namespace.QName

class SimuleringOppdragConfig(config: Config, private val enableLogging: Boolean = false) {

    private val simuleringOppdragUrl = config.simuleringOppdragUrl
    private val sts = config.sts
    private val logger = LoggerFactory.getLogger(javaClass)

    fun createSimuleringOppdragService(): SimulerFpService {
        logger.info("Bruker simuleringOppdrag url $simuleringOppdragUrl")

        val enabledFeatures = mutableListOf<Feature>().apply {
            add(WSAddressingFeature())
            if (enableLogging) add(LoggingFeature().apply {
                setSensitiveDataHelper(SoapSecurityMaskSensitiveHelper())
                setVerbose(true)
                setPrettyLogging(true)
                setSensitiveElementNames(setOf("oppdragGjelderId", "utbetalesTilId"))
            })
        }

        return JaxWsProxyFactoryBean().apply {
            address = simuleringOppdragUrl
            wsdlURL = WSDL
            serviceName = SERVICE
            endpointName = PORT
            serviceClass = SimulerFpService::class.java
            features = enabledFeatures
        }.wrapInStsClient(sts.soapUrl, ServiceUserConfig(sts.serviceuser.name, sts.serviceuser.password), true)
    }

    private companion object {
        private const val WSDL = "wsdl/no/nav/system/os/eksponering/simulerfpservicewsbinding.wsdl"
        private const val NAMESPACE = "http://nav.no/system/os/eksponering/simulerFpServiceWSBinding"
        private val SERVICE = QName(NAMESPACE, "simulerFpService")
        private val PORT = QName(NAMESPACE, "simulerFpServicePort")
    }
}
