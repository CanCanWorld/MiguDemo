package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.SearchSingerAdapter
import com.zrq.migudemo.adapter.SearchSongAdapter
import com.zrq.migudemo.bean.SearchSinger
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.dao.SongDaoImpl
import com.zrq.migudemo.databinding.FragmentSearchResultBinding
import com.zrq.migudemo.db.SongDatabaseHelper
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnSongMoreClickListener
import com.zrq.migudemo.util.Constants.BASE_URL
import com.zrq.migudemo.util.Constants.SEARCH
import com.zrq.migudemo.util.Constants.TYPE_SINGER
import com.zrq.migudemo.util.Constants.TYPE_SONG
import okhttp3.*
import java.io.IOException

class SearchResultFragment(
    var type: Int
) : BaseFragment<FragmentSearchResultBinding>(), OnSongMoreClickListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchResultBinding {
        return FragmentSearchResultBinding.inflate(inflater, container, false)
    }

    private lateinit var songAdapter: SearchSongAdapter
    private lateinit var singerAdapter: SearchSingerAdapter
    private val listSong = ArrayList<SearchSong.MusicsDTO>()
    private val listSinger = ArrayList<SearchSinger.ArtistsDTO>()
    private var key = ""
    private var offset = 1
    private lateinit var songDaoImpl: SongDaoImpl

    override fun initData() {
        songDaoImpl = SongDaoImpl(SongDatabaseHelper(requireContext()))
        songAdapter = SearchSongAdapter(requireContext(), listSong, object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                mainModel.playList.clear()
                mainModel.playList.addAll(listSong)
                mainModel.getOnSongChangeListener().onSongChange(listSong[position])
            }
        }, this)
        singerAdapter =
            SearchSingerAdapter(requireContext(), listSinger, object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    mainModel.artistId.postValue(listSinger[position].id)
                    Navigation.findNavController(requireActivity(), R.id.fragment_container)
                        .navigate(R.id.action_global_singerInfoFragment)
                }

            })
        when (type) {
            TYPE_SONG -> {
                mBinding.recyclerView.adapter = songAdapter
            }
            TYPE_SINGER -> {
                mBinding.recyclerView.adapter = singerAdapter
            }
            else -> {}
        }
        mBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mBinding.refreshLayout.setRefreshHeader(ClassicsHeader(requireContext()))
        mBinding.refreshLayout.setRefreshFooter(ClassicsFooter(requireContext()))
    }

    override fun initEvent() {
        mainModel.key.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (t != null) {
                    key = t
                    offset = 1
                    load()
                }
            }
        })

        mBinding.refreshLayout.setOnRefreshListener {
            offset = 1
            load()
        }
        mBinding.refreshLayout.setOnLoadMoreListener {
            offset++
            load()
        }

    }

    private fun load() {
        if (key.isNotEmpty()) {
            val url = "$BASE_URL$SEARCH?key=$key&type=$type&offset=$offset"
            Log.d(SearchFragment.TAG, "initEvent: $url")
            val request: Request = Request.Builder()
                .url(url)
                .get()
                .build()
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "onFailure: ")
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        val json = response.body!!.string()
                        Log.d(TAG, "onResponse: $json")
                        when (type) {
                            TYPE_SONG -> {
                                val song = Gson().fromJson(json, SearchSong::class.java)
                                if (song != null && song.musics != null) {
                                    if (offset == 1)
                                        listSong.clear()
                                    listSong.addAll(song.musics)
                                    requireActivity().runOnUiThread {
                                        songAdapter.notifyDataSetChanged()
                                        mBinding.refreshLayout.finishRefresh()
                                        mBinding.refreshLayout.finishLoadMore()
                                    }
                                }
                            }
                            TYPE_SINGER -> {
                                val singer = Gson().fromJson(json, SearchSinger::class.java)
                                if (singer != null && singer.artists != null) {
                                    listSinger.clear()
                                    listSinger.addAll(singer.artists)
                                    requireActivity().runOnUiThread {
                                        singerAdapter.notifyDataSetChanged()
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                }
            })
        }
    }

    companion object {
        const val TAG = "SearchResultFragment"
        fun newInstance(type: Int) = SearchResultFragment(type)
    }

    override fun onSongMoreClick(song: SearchSong.MusicsDTO, position:Int) {
        songDaoImpl.addSong(song)
    }
}