package com.zrq.migudemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zrq.migudemo.bean.Song
import com.zrq.migudemo.ui.PlayBarFragment

class PlayBarAdapter(
    fragmentActivity: FragmentActivity,
    var list: ArrayList<Song.DataDTO>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return PlayBarFragment.getInstance()
    }

}