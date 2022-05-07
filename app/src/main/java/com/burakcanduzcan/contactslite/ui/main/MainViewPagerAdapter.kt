package com.burakcanduzcan.contactslite.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    private val fragmentList = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    internal fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }
}