package com.op.score18xxphone

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

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
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Games.currentGameIndex = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Players.removeCallback(this)
        _binding = null
    }
}
