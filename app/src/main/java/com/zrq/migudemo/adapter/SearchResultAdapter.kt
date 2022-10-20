package com.zrq.migudemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zrq.migudemo.ui.SearchResultFragment

class SearchResultAdapter(
    fragmentManager: FragmentManager,
    var list: ArrayList<SearchResultFragment>,
    var titles: ArrayList<String>
) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}