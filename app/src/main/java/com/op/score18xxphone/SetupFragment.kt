package com.op.score18xxphone

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.op.score18xxphone.databinding.FragmentSetupBinding

class SetupFragment : Fragment() {
    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlayersAdapter
    private var lastGameIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        adapter = PlayersAdapter()
        binding.playerListView.adapter = adapter
        Players.addCallback(this) { adapter.notifyDataSetChanged() }
    }

    private fun setupSpinner() {
        val games = Games.readGames(requireActivity())
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_game_item, games.map { it.fullName })
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_game_dropdown_item)

        if (binding.game.adapter == null) {
            binding.game.adapter = spinnerAdapter
        }
        binding.game.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (lastGameIndex != -1 && lastGameIndex != position) {
                    Players.players.forEach { it.cash = 0 }
                    Games.resetGame(position)
                    Players.changeHappened()
                    Games.changeHappened()
                }
                lastGameIndex = position
                Games.currentGameIndex = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Players.removeCallback(this)
        _binding = null
    }
}
