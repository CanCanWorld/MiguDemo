package com.zrq.migudemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zrq.migudemo.adapter.SearchResultAdapter
import com.zrq.migudemo.databinding.FragmentSearchBinding
import com.zrq.migudemo.util.Constants.TYPE_ALBUM
import com.zrq.migudemo.util.Constants.TYPE_LYRICS
import com.zrq.migudemo.util.Constants.TYPE_MV
import com.zrq.migudemo.util.Constants.TYPE_SINGER
import com.zrq.migudemo.util.Constants.TYPE_SONG
import com.zrq.migudemo.util.Constants.TYPE_SONG_LIST

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    lateinit var adapter: SearchResultAdapter

    override fun initData() {
        val fragmentList = ArrayList<SearchResultFragment>()
        val titles = ArrayList<String>()
        fragmentList.add(SearchResultFragment.newInstance(TYPE_SONG))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_SINGER))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_SONG_LIST))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_ALBUM))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_MV))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_LYRICS))
        titles.add("歌曲")
        titles.add("歌手")
        titles.add("歌单")
        titles.add("专辑")
        titles.add("MV")
        titles.add("歌词")
        adapter = SearchResultAdapter(childFragmentManager, fragmentList, titles)
        mBinding.viewPager.adapter = adapter
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }

    override fun initEvent() {

        mBinding.btnSearch.setOnClickListener {
            val key = mBinding.etSearch.text.toString()
            mainModel.key.postValue(key)
        }
    }

    companion object {
        const val TAG = "SearchFragment"
    }

}