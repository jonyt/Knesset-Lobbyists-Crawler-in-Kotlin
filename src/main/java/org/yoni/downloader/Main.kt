package org.yoni.downloader

import com.github.salomonbrys.kotson.registerTypeAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.machinepublishers.jbrowserdriver.JBrowserDriver
import org.yoni.downloader.data.Client
import org.yoni.downloader.data.Field
import org.yoni.downloader.data.LobbyingFirm
import org.yoni.downloader.data.Lobbyist
import java.io.File

/**
 * Created by yoni on 10/04/16.
 *
 * An application that transforms data about lobbyists from the URL below to JSON.
 */
val baseUrl = "https://www.knesset.gov.il/lobbyist/heb/lobbyist.aspx"

fun main(args : Array<String>) {
    val gson = GsonBuilder().registerTypeAdapter<Lobbyist> {
        serialize {
            val result = JsonObject()
            result.addProperty("name", it.src.name)

            result
        }
    }.registerTypeAdapter<LobbyingFirm>{
        serialize {
            val result = JsonObject()
            result.addProperty("name", it.src.name)
            result.addProperty("number", it.src.number)
            result.add("lobbyists", it.context.serialize(it.src.lobbyists))

            result
        }
    }.registerTypeAdapter<Client>{
        serialize {
            val result = JsonObject()
            result.addProperty("name", it.src.name)
            result.add("lobbyingFirms", it.context.serialize(it.src.lobbyingFirms))

            result
        }
    }.registerTypeAdapter<Field>{
        serialize {
            val result = JsonObject()
            result.addProperty("name", it.src.name)
            result.add("clients", it.context.serialize(it.src.clients))

            result
        }
    }
            .create()

    val driver = JBrowserDriver()
    driver.get(baseUrl)

    val fields = mutableListOf<Field>()

    0.rangeTo(1).forEach {
        val currentLobbyistElement = driver.findElementsByClassName("link4").get(it)
        currentLobbyistElement.click()
        Thread.sleep(3000)
        val parser = Parser(driver, it)

        parser.companies.forEach {
            val (clientName, fieldName, lobbyingType) = it.split(" - ")

            fields.add(Field(fieldName, mutableSetOf<Client>()))
            val field = fields.find { it.name.equals(fieldName) }!!

            field.clients.add(Client(clientName, mutableSetOf<LobbyingFirm>()))
            val client = field.clients.find { it.name.equals(clientName) }!!

            client.lobbyingFirms.add(LobbyingFirm(parser.companyName, parser.companyNumber, mutableSetOf<Lobbyist>()))
            val lobbyingFirm = client.lobbyingFirms.find { it.name.equals(parser.companyName) }!!

            lobbyingFirm.lobbyists.add(Lobbyist(parser.lobbyistName))
        }
    }

    driver.quit()

    File(System.getProperty("java.io.tmpdir"), "lobbyingData.json").writeText(gson.toJson(fields))
}