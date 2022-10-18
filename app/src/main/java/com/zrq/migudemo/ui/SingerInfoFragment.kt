package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.SongOfSingerAdapter
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.Singer
import com.zrq.migudemo.bean.SongOfSinger
import com.zrq.migudemo.dao.SongDaoImpl
import com.zrq.migudemo.databinding.FragmentSingerInfoBinding
import com.zrq.migudemo.db.SongDatabaseHelper
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.interfaces.OnMoreClickListener
import com.zrq.migudemo.util.Constants.BASE_URL
import com.zrq.migudemo.util.Constants.SINGER_INFO
import com.zrq.migudemo.util.Constants.SINGER_SONG_LIST
import com.zrq.migudemo.view.DownloadDialog
import okhttp3.*
import java.io.IOException

class SingerInfoFragment : BaseFragment<FragmentSingerInfoBinding>(),
    OnItemClickListener, OnMoreClickListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSingerInfoBinding {
        return FragmentSingerInfoBinding.inflate(inflater, container, false)
    }

    private val list = ArrayList<SongOfSinger.SongListBean>()
    private val listSong = ArrayList<SearchSong.MusicsDTO>()
    private lateinit var adapter: SongOfSingerAdapter
    private var offset = 1
    private var artistId = ""
    private var downloadDialog: DownloadDialog? = null
    private lateinit var songDaoImpl: SongDaoImpl

    override fun initData() {
        songDaoImpl = SongDaoImpl(SongDatabaseHelper(requireContext()))
        adapter = SongOfSingerAdapter(requireContext(), list, this, this)
        mBinding.rvSongOfSinger.adapter = adapter
        mBinding.rvSongOfSinger.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initEvent() {
        mainModel.artistId.observe(this) {
            if (it != null) {
                artistId = it
                loadSinger()
                loadSongOfSinger()
            }
        }
        mBinding.apply {
            refreshLayout.setOnRefreshListener {
                offset = 1
                loadSongOfSinger()
            }
            refreshLayout.setOnLoadMoreListener {
                offset++
                loadSongOfSinger()
            }
        }
    }

    private fun loadSinger() {
        val url = "$BASE_URL$SINGER_INFO?artistId=$artistId"
        Log.d(TAG, "initEvent: $url")
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
                    val singer = Gson().fromJson(json, Singer::class.java)
                    if (singer != null && singer.data != null) {
                        requireActivity().runOnUiThread {
                            refreshSingerInfo(singer.data)
                        }
                    }
                }
            }
        })
    }

    private fun loadSongOfSinger() {
        val url = "$BASE_URL$SINGER_SONG_LIST?artistId=$artistId&offset=$offset"
        Log.d(TAG, "initEvent: $url")
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                finishRefreshOrLoad()
                Log.d(TAG, "onFailure: ")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call, response: Response) {
                if (response.body != null) {
                    val json = response.body!!.string()
                    Log.d(TAG, "onResponse: $json")
                    val songOfSinger = Gson().fromJson(json, SongOfSinger::class.java)
                    if (songOfSinger != null && songOfSinger.songList != null) {
                        if (offset == 1)
                            list.clear()
                        list.addAll(songOfSinger.songList)
                        listSong.clear()
                        for (i in list.indices) {
                            val s = list[i]
                            listSong.add(SearchSong.MusicsDTO().apply {
                                songName = s.al.title
                                albumName = s.al.album
                                singerName = s.al.singer
                                cover = "https://" + s.al.imgUrl.removePrefix("//")
                                copyrightId = s.copyrightId
                            })
                        }
                        adapter.notifyDataSetChanged()
                        finishRefreshOrLoad()
                    }else{
                        finishRefreshOrLoad()
                    }
                }else{
                    finishRefreshOrLoad()
                }
            }
        })
    }

    private fun finishRefreshOrLoad() {
        requireActivity().runOnUiThread {
            mBinding.refreshLayout.finishRefresh()
            mBinding.refreshLayout.finishLoadMore()
        }
    }

    private fun refreshSingerInfo(data: Singer.DataDTO) {
        mBinding.apply {
            collapsingToolbar.title = data.anotherName
            Glide.with(this@SingerInfoFragment)
                .load(data.localArtistPicL)
                .into(ivHead)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPopMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view, Gravity.END)
        popupMenu.menuInflater.inflate(R.menu.menu_song_long_click, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_love -> {
                    songDaoImpl.addSong(listSong[position])
                }
                R.id.menu_download -> {
                    if (downloadDialog == null) {
                        downloadDialog = DownloadDialog(requireContext(), requireActivity())
                    }
                    downloadDialog!!.downloadSong = listSong[position]
                    downloadDialog!!.setTitle("选择下载音质")
                    downloadDialog!!.show()
                }
                else -> {}
            }
            true
        }
        popupMenu.show()
    }

    companion object {
        const val TAG = "SingerInfoFragment"
    }

    override fun onItemClick(view: View, position: Int) {
        mainModel.setList(listSong)
        mainModel.onSongChangeListener?.onSongChange(position)
    }

    override fun onMoreClick(view: View, position: Int) {
        showPopMenu(view, position)
    }
}