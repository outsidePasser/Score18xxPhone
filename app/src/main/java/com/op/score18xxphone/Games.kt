package com.op.score18xxphone

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.io.File
import kotlin.arrayOf

data class Game(val title: String, val fullName: String, val stockPrices: Array<Int>, val companies: Array<Company>)

object Games {
    var games: Array<Game> = arrayOf<Game>()
    var currentGameIndex = 0
    lateinit var callback: () -> Unit

    fun readGames(application: Activity): Array<Game> {
        if (games.size == 0) {
            readGameFile(application)
        }
        return games
    }

    private fun readGameFile(application: Activity) {
        var json_string : String = ""
        json_string = application.assets.open("game_data.json").bufferedReader().use{
            it.readText() }
        var gson = Gson()
        var arrayType: Type = object : TypeToken<Array<Game?>?>() {}.type
        games = gson.fromJson(json_string, arrayType)

        games.forEach { game -> game.companies.forEach { company -> company.runs = arrayOf(0,0,0) } }
    }

    fun changeHappened() {
        callback()
    }
}