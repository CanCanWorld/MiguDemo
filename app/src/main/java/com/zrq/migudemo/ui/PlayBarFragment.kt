package com.zrq.migudemo.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zrq.migudemo.R
import com.zrq.migudemo.bean.Lyric
import com.zrq.migudemo.bean.LyricByTime
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.FragmentPlayBarBinding
import com.zrq.migudemo.interfaces.OnLyricChangeListener
import com.zrq.migudemo.util.Constants
import okhttp3.*
import java.io.IOException

class PlayBarFragment
    (var position: Int) : BaseFragment<FragmentPlayBarBinding>(), OnLyricChangeListener {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayBarBinding {
        return FragmentPlayBarBinding.inflate(inflater, container, false)
    }

    private lateinit var animation: ValueAnimator

    override fun initData() {
        initAnimation()
        refreshPlayBar()
    }

    override fun initEvent() {

        mainModel.onLyricChangeListener = this

        mBinding.apply {
            llPlayBar.setOnClickListener {
            }
        }

        mainModel.nowPlaying.observe(this) {
            if (it != null) {
                loadLyric(it)
            }
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

    private fun refreshPlayBar() {
        val song = mainModel.getList()?.get(position)
        if (song != null) {
            mBinding.apply {
                llPlayBar.visibility = View.VISIBLE
                ivAlbum.visibility = View.VISIBLE
                tvSongName.text = song.songName
                tvSinger.text = song.singerName
                Log.d(TAG, "refreshPlayBar: ${song.cover}")
                Glide.with(this@PlayBarFragment)
                    .load(song.cover)
                    .into(ivAlbum)
                animation.start()
            }
        }
    }

    private fun loadLyric(musicsDTO: SearchSong.MusicsDTO) {
        val cid = musicsDTO.copyrightId
        if (cid.isNotEmpty()) {
            val url = "${Constants.BASE_URL}${Constants.LYRIC}?cid=$cid"
            Log.d(HomeFragment.TAG, "loadLyric: $url")
            val request: Request = Request.Builder()
                .url(url)
                .get()
                .build()
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(HomeFragment.TAG, "onFailure: ")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        val json = response.body!!.string()
                        val lyric = Gson().fromJson(json, Lyric::class.java)
                        if (lyric != null && lyric.lyric != null) {
                            setLyric(lyric.lyric)
                        }
                    }
                }
            })
        }
    }

    private var lyricList = ArrayList<LyricByTime>()

    private fun setLyric(lyric: String) {
        val regex = Regex("\r\n")
        val split = lyric.split(regex)
        lyricList.clear()
        split.forEach {
            if (it.length > 10) {
                Log.d(TAG, "setLyric: $it")
                try {
                    val min = it.substring(1, 3).toInt()
                    val s = it.substring(4, 6).toInt()
                    val ms = it.substring(7, 9).toInt()
                    val string = it.substring(10, it.length)
                    val time = min * 60 * 1000 + s * 1000 + ms
                    val lyricByTime = LyricByTime(time, string)
                    lyricList.add(lyricByTime)
                } catch (e: Exception) {
                    Log.d(TAG, "setLyric: $e")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: $position")
        refreshPlayBar()
    }

    companion object {
        const val TAG = "PlayBarFragment"

        fun getInstance(position: Int) = PlayBarFragment(position)
    }

    override fun onLyricChange(elapsedTime: Int) {
        for (i in lyricList.indices) {
            if (i + 1 != lyricList.size) {
                if (elapsedTime > lyricList[i].time && elapsedTime < lyricList[i + 1].time) {
                    requireActivity().runOnUiThread {
                        mBinding.tvLyric.text = lyricList[i].lyric
                    }
                }
            } else if (elapsedTime > lyricList[i].time) {
                requireActivity().runOnUiThread {
                    mBinding.tvLyric.text = lyricList[i].lyric
                }
            }
        }
    }

}
