package com.op.score18xxphone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.op.score18xxphone.databinding.FragmentSetupBinding

class SetupFragment : Fragment() {
    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentSetupBinding.inflate(layoutInflater)
        setupSpinner()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Players.callback = ::playerListChanged
        updatePlayerList(view)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupSpinner() {
        var gameData = (activity as MainActivity).getGameData()
        var adapter = ArrayAdapter(requireContext(), R.layout.spinner_game_item, gameData.map { it.fullName } )
        adapter.setDropDownViewResource(R.layout.spinner_game_dropdown_item)

        if (binding.game.adapter == null) {
            binding.game.adapter = adapter
        }
        binding.game.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                (activity as MainActivity).selectGameByIndex(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun updatePlayerList(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.player_list_view)
        val adapter = PlayersAdapter()
        recyclerView.adapter = adapter
    }

    private fun playerListChanged() {
        updatePlayerList(binding.root)
    }
}