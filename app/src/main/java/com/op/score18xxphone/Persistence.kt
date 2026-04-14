package com.op.score18xxphone

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson

object Persistence {
    private const val PREFS_NAME = "score18xx"
    private const val KEY_STATE = "state"

    private data class CompanyState(
        val stockPrice: Int,
        val runs: List<Int>?,
        val runsExplicitlySet: List<Boolean>?,
        val shares: List<Int>?
    )

    private data class PlayerState(val name: String, val cash: Int)

    private data class AppState(
        val players: List<PlayerState>,
        val gameStates: Map<String, List<CompanyState>>
    )

    fun save() {
        if (Games.games.isEmpty()) return
        val gson = Gson()
        val playerStates = Players.players.map { PlayerState(it.name, it.cash) }
        val gameStates = Games.games.mapIndexed { gameIndex, game ->
            gameIndex.toString() to game.companies.map { company ->
                CompanyState(
                    stockPrice = company.stockPrice,
                    runs = company.runs.toList(),
                    runsExplicitlySet = company.runsExplicitlySet.toList(),
                    shares = company.shares.toList()
                )
            }
        }.toMap()
        val json = gson.toJson(AppState(playerStates, gameStates))
        App.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putString(KEY_STATE, json)
        }
    }

    fun load() {
        val json = App.context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_STATE, null) ?: return
        try {
            val state = Gson().fromJson(json, AppState::class.java)

            Players.players.clear()
            state.players.forEach { p -> Players.players.add(Player(p.name, p.cash)) }

            state.gameStates.forEach { (gameIndexStr, companyStates) ->
                val gameIndex = gameIndexStr.toIntOrNull() ?: return@forEach
                val game = Games.games.getOrNull(gameIndex) ?: return@forEach
                companyStates.forEachIndexed { companyIndex, cs ->
                    val company = game.companies.getOrNull(companyIndex) ?: return@forEachIndexed
                    company.stockPrice = cs.stockPrice
                    company.runs = cs.runs?.toMutableList() ?: mutableListOf(0, 0, 0)
                    company.runsExplicitlySet = cs.runsExplicitlySet?.toMutableList() ?: mutableListOf(false, false, false)
                    company.shares = cs.shares?.toMutableList() ?: mutableListOf()
                }
            }
        } catch (e: Exception) {
            Log.w("Persistence", "Corrupt or incompatible saved state — starting fresh", e)
        }
    }
}
