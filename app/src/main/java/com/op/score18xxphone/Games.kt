package com.op.score18xxphone

import android.app.Activity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class Game(val title: String, val fullName: String, val stockPrices: List<Int>, val companies: List<Company>)

object Games {
    var games: List<Game> = emptyList()
    var currentGameIndex = 0

    private var initialStockPrices: List<List<Int>> = emptyList()
    private val callbacks = LinkedHashMap<Any, () -> Unit>()

    fun addCallback(owner: Any, callback: () -> Unit) {
        callbacks[owner] = callback
    }

    fun removeCallback(owner: Any) {
        callbacks.remove(owner)
    }

    fun readGames(application: Activity): List<Game> {
        if (games.isEmpty()) {
            readGameFile(application)
        }
        return games
    }

    private fun readGameFile(application: Activity) {
        val jsonString = application.assets.open("game_data.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val listType: Type = object : TypeToken<List<Game>>() {}.type
        games = gson.fromJson(jsonString, listType)

        games.forEach { game ->
            game.companies.forEach { company ->
                company.runs = mutableListOf(0, 0, 0)
                company.runsExplicitlySet = mutableListOf(false, false, false)
                company.shares = mutableListOf()
            }
        }

        initialStockPrices = games.map { game -> game.companies.map { it.stockPrice } }

        Persistence.load()
    }

    fun resetGame(gameIndex: Int) {
        val game = games.getOrNull(gameIndex) ?: return
        val origPrices = initialStockPrices.getOrNull(gameIndex) ?: return
        game.companies.forEachIndexed { i, company ->
            company.stockPrice = origPrices.getOrElse(i) { company.stockPrice }
            company.runs = mutableListOf(0, 0, 0)
            company.runsExplicitlySet = mutableListOf(false, false, false)
            company.shares = mutableListOf()
        }
    }

    fun changeHappened() {
        callbacks.values.toList().forEach { it() }
    }
}
