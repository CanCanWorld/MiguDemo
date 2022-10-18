package com.zrq.migudemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.zrq.migudemo.adapter.SearchResultAdapter
import com.zrq.migudemo.databinding.FragmentSearchBinding
import com.zrq.migudemo.util.Constants.TYPE_ALBUM
import com.zrq.migudemo.util.Constants.TYPE_LYRICS
import com.zrq.migudemo.util.Constants.TYPE_MV
import com.zrq.migudemo.util.Constants.TYPE_SINGER
import com.zrq.migudemo.util.Constants.TYPE_SONG
import com.zrq.migudemo.util.Constants.TYPE_SONG_LIST

class SearchFragment : BaseFragment<FragmentSearchBinding>(){
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    lateinit var searchAdapter: SearchResultAdapter

    override fun initData() {

        initTabLayout()

    }

    override fun initEvent() {

        mBinding.apply {

            btnSearch.setOnClickListener {
                val key = etSearch.text.toString()
                mainModel.key.postValue(key)
            }

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    btnSearch.callOnClick()
                }
                false
            }

            ibDelete.setOnClickListener {
                etSearch.setText("")
            }


        }

    }


    private fun initTabLayout() {
        val fragmentList = ArrayList<SearchResultFragment>()
        val titles = ArrayList<String>()
        fragmentList.add(SearchResultFragment.newInstance(TYPE_SONG))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_SINGER))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_SONG_LIST))
        fragmentList.add(SearchResultFragment.newInstance(TYPE_ALBUM))
        titles.add("歌曲")
        titles.add("歌手")
        titles.add("歌单")
        titles.add("专辑")
        searchAdapter = SearchResultAdapter(childFragmentManager, fragmentList, titles)
        mBinding.viewPager.adapter = searchAdapter
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }



    companion object {
        const val TAG = "SearchFragment"
    }

}