package org.yoni.downloader.data

/**
 * Created by yoni on 05/05/16.
 */
class Field(name: String, val clients: MutableSet<Client>) : Element(name) {
}