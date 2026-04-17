package com.op.score18xxphone

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.op.score18xxphone.databinding.FragmentSetupBinding

private const val MAX_PLAYERS = 8

class SetupFragment : Fragment() {
    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!

    private var lastGameIndex = -1

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
        setupAddPlayer()
        Players.addCallback(this) { rebuildChips() }
        rebuildChips()
    }

    private fun rebuildChips() {
        binding.playerChipGroup.removeAllViews()
        Players.players.forEachIndexed { index, player ->
            val chip = Chip(requireContext()).apply {
                text = player.name
                isCloseIconVisible = true
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.dark_gray)
                )
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                closeIconTint = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.light_gray)
                )
                textSize = 24f
                setOnCloseIconClickListener { Players.removePlayerByIndex(index) }
            }
            binding.playerChipGroup.addView(chip)
        }
        binding.addPlayerRow.visibility =
            if (Players.players.size < MAX_PLAYERS) View.VISIBLE else View.GONE
    }

    private fun setupAddPlayer() {
        fun addPlayer() {
            val name = binding.playerNameInput.text.toString().trim()
            if (name.isNotEmpty()) {
                Players.addPlayerByName(name)
                binding.playerNameInput.text.clear()
            }
        }

        binding.addPlayerButton.setOnClickListener { addPlayer() }
        binding.playerNameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addPlayer()
                true
            } else {
                false
            }
        }
    }

    private fun setupSpinner() {
        val games = Games.readGames(requireActivity())
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_game_item, games.map { it.fullName })
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_game_dropdown_item)

        if (binding.game.adapter == null) {
            binding.game.adapter = spinnerAdapter
        }
        binding.game.setSelection(Games.currentGameIndex, false)
        lastGameIndex = Games.currentGameIndex
        binding.game.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (lastGameIndex != -1 && lastGameIndex != position) {
                    Players.resetAllCash()
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
