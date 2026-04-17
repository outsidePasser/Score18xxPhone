package com.op.score18XXphone

data class Player(val name: String, var cash: Int = 0, var otherAssets: Int = 0)

object Players {
    private val _players: MutableList<Player> = mutableListOf()
    val players: List<Player> get() = _players

    private val callbacks = LinkedHashMap<Any, () -> Unit>()

    fun addCallback(owner: Any, callback: () -> Unit) {
        callbacks[owner] = callback
    }

    fun removeCallback(owner: Any) {
        callbacks.remove(owner)
    }

    fun addPlayerByName(name: String) {
        _players += Player(name)
        notifyCallbacks()
    }

    fun removePlayerByIndex(index: Int) {
        _players.removeAt(index)
        notifyCallbacks()
    }

    fun restorePlayers(newPlayers: List<Player>) {
        _players.clear()
        _players.addAll(newPlayers)
    }

    fun resetAllCash() {
        _players.forEach { it.cash = 0 }
    }

    fun changeHappened() {
        notifyCallbacks()
    }

    private fun notifyCallbacks() {
        callbacks.values.toList().forEach { it() }
    }
}
