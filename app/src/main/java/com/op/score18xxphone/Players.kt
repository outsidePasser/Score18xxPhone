package com.op.score18xxphone

data class Player(val name: String, var cash: Int = 0)

object Players {
    var players: MutableList<Player> = mutableListOf()

    private val callbacks = LinkedHashMap<Any, () -> Unit>()

    fun addCallback(owner: Any, callback: () -> Unit) {
        callbacks[owner] = callback
    }

    fun removeCallback(owner: Any) {
        callbacks.remove(owner)
    }

    fun addPlayerByName(name: String) {
        players += Player(name)
        notifyCallbacks()
    }

    fun removePlayerByIndex(index: Int) {
        players.removeAt(index)
        notifyCallbacks()
    }

    private fun notifyCallbacks() {
        callbacks.values.toList().forEach { it() }
    }
}
