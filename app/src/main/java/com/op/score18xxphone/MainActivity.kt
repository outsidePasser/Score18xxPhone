package com.op.score18xxphone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.op.score18xxphone.Player
import com.op.score18xxphone.databinding.ActivityMainBinding
import java.lang.reflect.Type

data class Company(val name: String, val color: String, val textColor: String)
data class Game(val title: String, val fullName: String, val stockPrices: Array<Int>, val companies: Array<Company>)

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var gameData : Array<Game> = arrayOf<Game>()
    private var players : MutableList<Player> = mutableListOf<Player>()
    private var selectedGame : Int = 0
    private val setupFragment : Fragment = SetupFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = this.findNavController(R.id.nav_host_fragment)
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.setupWithNavController(navController)
        true
    }

    private fun readGameData() {
        var json_string : String = ""
        json_string = application.assets.open("game_data.json").bufferedReader().use{
            it.readText() }
        var gson = Gson()
        var arrayType: Type = object : TypeToken<Array<Game?>?>() {}.type
        gameData = gson.fromJson(json_string, arrayType)
    }

    fun getGameData(): Array<Game> {
        if (gameData.isEmpty()) {
            readGameData()
        }
        return gameData
    }
    fun selectGameByIndex(index : Int) { selectedGame = index }
    fun selectedGameIndex(): Int { return selectedGame }


}


