package com.op.score18xxphone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.op.score18xxphone.databinding.FragmentCompaniesBinding

class CompaniesFragment : Fragment() {
    private var _binding: FragmentCompaniesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CompaniesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompaniesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CompaniesAdapter()
        binding.companyListView.adapter = adapter
        Games.addCallback(this) { adapter.notifyDataSetChanged() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Games.removeCallback(this)
        _binding = null
    }
}
