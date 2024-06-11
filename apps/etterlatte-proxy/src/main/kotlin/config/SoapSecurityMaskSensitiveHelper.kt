package no.nav.etterlatte.config

import org.apache.cxf.ext.logging.MaskSensitiveHelper
import org.apache.cxf.message.Message
import java.util.regex.Pattern

/**
 * Handle tag with multiline content between opening and end.
 */
internal class SoapSecurityMaskSensitiveHelper : MaskSensitiveHelper() {
    private val matchPatternXML = Pattern.compile("(<wsse:Security.*?>)([\\s\\S.]*?)(</wsse:Security>)")

    override fun maskSensitiveElements(
        message: Message,
        originalLogString: String
    ): String {
        val resultString = matchPatternXML.matcher(originalLogString).replaceAll("$1***REDACTED***$3")

        return super.maskSensitiveElements(message, resultString)
    }
}