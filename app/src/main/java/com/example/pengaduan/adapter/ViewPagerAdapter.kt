package com.example.pengaduan.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pengaduan.fragment.TabDiterimaFragment
import com.example.pengaduan.fragment.TabDitolakFragment
import com.example.pengaduan.fragment.TabMenungguFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()

        when(position){
            0 -> fragment = TabMenungguFragment()
            1 -> fragment = TabDiterimaFragment()
            2 -> fragment = TabDitolakFragment()
        }

        return fragment
    }

}