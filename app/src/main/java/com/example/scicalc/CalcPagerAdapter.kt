package com.example.scicalc

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalcPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int) = when (position) {
        0 -> BasicCalculatorFragment()
        1 -> ScientificCalculatorFragment()
        2 -> MatrixFragment()
        else -> StatsCombinatoricsFragment()
    }
}
