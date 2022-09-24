package com.zrq.migudemo.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.Song
import com.zrq.migudemo.databinding.FragmentPlayBinding
import com.zrq.migudemo.util.Constants
import okhttp3.*
import java.io.IOException

class PlayFragment : BaseFragment<FragmentPlayBinding>() {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayBinding {
        return FragmentPlayBinding.inflate(inflater, container, false)
    }

    private lateinit var animation: ValueAnimator
    private lateinit var nowPlaying: SearchSong.MusicsDTO

    override fun initData() {
        initAnimation()
        Log.d(TAG, "initData: ")
    }

    override fun initEvent() {
        mainModel.nowPlaying.observe(this) {
            if (it != null) {
                nowPlaying = it
                refresh()
                loadSong()
            }
        }


    }

    private fun loadSong() {
        val cid = nowPlaying.copyrightId
        if (cid.isNotEmpty()) {
            val url = "${Constants.BASE_URL}${Constants.SONG}?cid=$cid&br=2"
            Log.d(TAG, "loadSong: $url")
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
                        val song = Gson().fromJson(json, Song::class.java)
                        requireActivity().runOnUiThread {
                            mainModel.nowSong = song
                            mainModel.play()
                        }
                    }
                }
            })
        }

    }

    private fun refresh() {
        mBinding.apply {
            tvSongName.text = nowPlaying.songName
            tvSinger.text = nowPlaying.singerName
            Glide.with(requireActivity())
                .load(nowPlaying.cover)
                .into(ivAlbum)
            animation.start()
        }
    }

    private fun initAnimation() {
        animation = ObjectAnimator.ofFloat(mBinding.ivAlbum, "rotation", .0f, 360.0f)
        animation.apply {
            duration = 6000
            interpolator = LinearInterpolator()
            repeatCount = -1
            repeatMode = ObjectAnimator.RESTART
        }
    }

    override fun onPause() {
        super.onPause()
        animation.pause()
    }

    companion object {
        const val TAG = "PlayFragment"
    }

}
