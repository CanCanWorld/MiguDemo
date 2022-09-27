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
import com.zrq.migudemo.bean.Singer
import com.zrq.migudemo.bean.SongOfSinger
import com.zrq.migudemo.dao.SongDaoImpl
import com.zrq.migudemo.databinding.FragmentSingerInfoBinding
import com.zrq.migudemo.db.SongDatabaseHelper
import com.zrq.migudemo.interfaces.OnItemClickListener
import com.zrq.migudemo.util.Constants.BASE_URL
import com.zrq.migudemo.util.Constants.SINGER_INFO
import com.zrq.migudemo.util.Constants.SINGER_SONG_LIST
import okhttp3.*
import java.io.IOException

class SingerInfoFragment : BaseFragment<FragmentSingerInfoBinding>(), OnItemClickListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSingerInfoBinding {
        return FragmentSingerInfoBinding.inflate(inflater, container, false)
    }

    private val listSong = ArrayList<SongOfSinger.SongListBean>()
    private lateinit var adapter: SongOfSingerAdapter
    private var offset = 1
    private var artistId = ""

    override fun initData() {
        adapter = SongOfSingerAdapter(requireContext(), listSong, this)
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
                            listSong.clear()
                        listSong.addAll(songOfSinger.songList)
                        requireActivity().runOnUiThread {
                            adapter.notifyDataSetChanged()
                            mBinding.refreshLayout.finishRefresh()
                            mBinding.refreshLayout.finishLoadMore()
                        }
                    }
                }
            }
        })
    }

    private fun refreshSingerInfo(data: Singer.DataDTO) {
        mBinding.apply {
            tvSinger.text = data.anotherName
            tvIntro.text = data.intro
            Glide.with(this@SingerInfoFragment)
                .load(data.localArtistPicL)
                .into(ivHead)
        }
    }

    companion object {
        const val TAG = "SingerInfoFragment"
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(requireContext(), "click$position", Toast.LENGTH_SHORT).show()
    }
}