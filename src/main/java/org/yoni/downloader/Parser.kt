package org.yoni.downloader

import com.machinepublishers.jbrowserdriver.JBrowserDriver

/**
 * Created by yoni on 12/04/16.
 */
class Parser(val driver: JBrowserDriver, val lobbyistNumber: Int) {
    object IdSuffixes {
        const val LOBBYIST_NAME = "dlLobbyistDetailsC_ctl00_lblname"
        const val COMPANY_NAME = "dlLobbyistDetailsC_ctl00_lblCorporation_name"
        const val COMPANY_NUMBER = "dlLobbyistDetailsC_ctl00_lblCorporation_ID"
    }
    val lobbyistElements = driver.findElementsByClassName("link4")
    val currentLobbyistElement = lobbyistElements.get(lobbyistNumber)
    val lobbyistName = getValue(IdSuffixes.LOBBYIST_NAME)
    val companyName = getValue(IdSuffixes.COMPANY_NAME, "אין חברה")
    val companyNumber = getValue(IdSuffixes.COMPANY_NUMBER).replace("-", "").toLong()
    val companies = companies()

    private fun getValue(idSuffix: String, default: String = ""): String{
        val spanId = currentLobbyistElement.getAttribute("id").replace("lbNameC", idSuffix)
        val span = driver.findElementById(spanId)

        return span?.text ?: default
    }

    private fun companies(): List<String> {
        val elements = driver.findElementsByXPath("//span[contains(@id, \"lblrepresent\")]")
        return elements.map { it.text }
    }
}