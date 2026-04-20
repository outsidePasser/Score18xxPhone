package com.op.score18XXphone

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> SetupFragment()
        1 -> CompaniesFragment()
        2 -> PlayersFragment()
        else -> SummaryFragment()
    }
}
