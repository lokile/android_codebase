package com.lokile.applibraries.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

open class ViewPagerAdapter2 constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle, var fragmentList: List<Fragment>):
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}