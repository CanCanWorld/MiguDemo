package com.zrq.migudemo.ui

import android.animation.ValueAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.zrq.migudemo.MainModel
import com.zrq.migudemo.R
import com.zrq.migudemo.bean.Lyric
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.Song
import com.zrq.migudemo.databinding.FragmentPlayBinding
import com.zrq.migudemo.util.Constants
import com.zrq.migudemo.util.Constants.BASE_URL
import com.zrq.migudemo.util.Constants.LYRIC
import okhttp3.*
import java.io.IOException

class PlayFragment : BaseFragment<FragmentPlayBinding>() {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayBinding {
        return FragmentPlayBinding.inflate(inflater, container, false)
    }

    private lateinit var nowPlaying: SearchSong.MusicsDTO

    override fun initData() {
        if (mainModel.playSong != null) {
            nowPlaying = mainModel.playSong!!
            refresh()
        }
    }

    override fun initEvent() {
        mBinding.apply {
            ibBack.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .popBackStack()
            }

        }
    }

    private fun refresh() {
        mBinding.apply {
            tvSongName.text = nowPlaying.songName
            tvSinger.text = nowPlaying.singerName
        }
        loadLyric(nowPlaying)
    }

    private fun loadLyric(song: SearchSong.MusicsDTO) {
        val cid = song.copyrightId
        if (cid.isNotEmpty()) {
            val url = "${BASE_URL}${LYRIC}?cid=$cid"
            Log.d(TAG, "loadLyric: $url")
            val request: Request = Request.Builder()
                .url(url)
                .get()
                .build()
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "onFailure: ")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        val json = response.body!!.string()
                        Log.d(TAG, "onResponse: $json")
                        val lyric = Gson().fromJson(json, Lyric::class.java)
                        if (lyric != null && lyric.lyric != null) {
                            requireActivity().runOnUiThread {
                                mBinding.tvLyric.text = lyric.lyric
                            }
                        }
                    }
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {
        const val TAG = "PlayFragment"
    }

}
