package com.op.score18XXphone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class SummaryFragment : Fragment() {

    private lateinit var adapter: SummaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.summary_recycler_view)
        adapter = SummaryAdapter()
        recyclerView.adapter = adapter

        Games.addCallback(this) { adapter.refresh() }
        Players.addCallback(this) { adapter.refresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Games.removeCallback(this)
        Players.removeCallback(this)
    }
}
