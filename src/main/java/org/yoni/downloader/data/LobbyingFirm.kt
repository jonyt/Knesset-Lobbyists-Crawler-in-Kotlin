package org.yoni.downloader.data

/**
 * Created by yoni on 10/04/16.
 */
class LobbyingFirm(name: String, val number: Long, val lobbyists: MutableSet<Lobbyist>) : Element(name){
}