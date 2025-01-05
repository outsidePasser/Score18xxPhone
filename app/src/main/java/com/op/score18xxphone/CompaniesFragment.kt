package com.op.score18xxphone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.op.score18xxphone.databinding.FragmentCompaniesBinding
import kotlin.collections.forEach

class CompaniesFragment : Fragment() {
    private var _binding: FragmentCompaniesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentCompaniesBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //
        Games.callback = ::companyChanged
        updateCompanies(view)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateCompanies(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.company_list_view)
        val adapter = CompaniesAdapter()
        recyclerView.adapter = adapter
    }

    private fun companyChanged() {
        updateCompanies(binding.root)
    }


}