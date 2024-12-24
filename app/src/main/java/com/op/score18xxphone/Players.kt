package com.op.score18xxphone

data class Player(val name: String)

object Players {
    var players: MutableList<Player> = mutableListOf()
    lateinit var callback: () -> Unit

    fun addPlayerByName(name: String) {
        players += Player(name)
        callback()
    }

    fun removePlayerByIndex(index: Int) {
        players.removeAt(index)
        callback()
    }
}