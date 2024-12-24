package com.op.score18xxphone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.op.score18xxphone.databinding.FragmentSetupBinding

class OldSetupFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var binding : FragmentSetupBinding
    private lateinit var adapter : ArrayAdapter<String>

    init {
        Log.i("MHA fragment", "setup init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MHA fragment","onCreate, this = " + System.identityHashCode(this).toString())
        binding = FragmentSetupBinding.inflate(layoutInflater)

        mainActivity = activity as MainActivity
        adapter = ArrayAdapter(requireContext(), R.layout.game_spinner, mainActivity.getGameData().map { it.title })
        //mainActivity.getGameData().forEach { game -> Log.i("Game", game.title)}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.i("MHA fragment","onCreateView")
        var rootView = inflater.inflate(R.layout.fragment_setup, container, false)
        //setupSpinner()
        return rootView
    }

    private fun changeGame() {
        //Log.i("MHA fragment", "ChangeGame: " + binding.game.selectedItem.toString())
    }

    /*
    private fun setupSpinner() {

        var games = mainActivity.getGameData().map { it.title }

        //var adapter = ArrayAdapter(mainActivity, R.layout.game_spinner, games)
        //var adapter = ArrayAdapter(requireContext(), R.layout.game_spinner, games)
        //if (adapter == null) {
        //    Log.i("MHA", "adapter is NULL")
        //}

        binding.game.adapter = adapter
        Log.i("MHA fragment", "spinner class: " + binding.game::class.simpleName.toString() + " address: " + System.identityHashCode(binding.game).toString())
        //Log.i("MHA setup", binding.game.id.toString())
        //Log.i("MHA setup", binding.game.adapter::class.simpleName.toString())
        //Log.i("MHA setup", System.identityHashCode(binding.game).toString())

        binding.game.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                changeGame()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        //Log.i("MHA setup", binding.game.onItemSelectedListener.toString())

    }
*/
}