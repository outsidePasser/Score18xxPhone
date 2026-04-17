package com.op.score18XXphone

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class PlayersFragment : Fragment() {

    private lateinit var adapter: PlayerCardsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_players, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.players_recycler_view)
        adapter = PlayerCardsAdapter()
        recyclerView.adapter = adapter

        Games.addCallback(this) { adapter.notifyDataSetChanged() }
        Players.addCallback(this) { adapter.notifyDataSetChanged() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Games.removeCallback(this)
        Players.removeCallback(this)
    }
}
