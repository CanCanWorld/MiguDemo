package com.zrq.migudemo.helper

import android.media.MediaPlayer
import android.os.Binder
import android.util.Log
import com.google.gson.Gson
import com.zrq.migudemo.MainModel
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.Song
import com.zrq.migudemo.interfaces.IPlayerControl
import com.zrq.migudemo.interfaces.IPlayerViewControl
import com.zrq.migudemo.util.Constants
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

object PlayerHelper : Binder(), IPlayerControl {

    private var mViewControl: IPlayerViewControl? = null

    private var mMediaPlayer: MediaPlayer = MediaPlayer()

    private var playList = ArrayList<SearchSong.MusicsDTO>()

    private var nowPlayingPosition = 0

    private const val TAG = "PlayerHelper"

    private var mTimerTask: SeekTimeTask? = null

    private var timer: Timer? = null

    init {
        mMediaPlayer.setOnCompletionListener {
            if (nowPlayingPosition == playList.size - 1) {
                nowPlayingPosition = 0
            } else {
                nowPlayingPosition++
            }
            Log.d(TAG, "setOnCompletionListener: ")
//            resetMediaPlayer()
        }
    }

    override fun registerViewControl(viewControl: IPlayerViewControl) {
        mViewControl = viewControl
    }

    override fun unRegisterViewControl() {
        mViewControl = null
    }

    override fun play() {
        Log.d(TAG, "play: ")
        mMediaPlayer.setOnPreparedListener {
            mMediaPlayer.start()
            mViewControl?.onSongChange(mMediaPlayer.duration)
        }
        mMediaPlayer.start()
        startTimer()
        mViewControl?.onSongChange(mMediaPlayer.duration)
    }

    override fun pause() {
        mMediaPlayer.pause()
        stopTimer()
    }

    override fun next() {
        Log.d(TAG, "next: ")
        when (playList.size) {
            0 -> {
                return
            }
            1 -> {}
            else -> {
                if (nowPlayingPosition == playList.size - 1) {
                    nowPlayingPosition = 0
                } else {
                    nowPlayingPosition++
                }
            }
        }
        resetMediaPlayer()
    }

    override fun pre() {
        Log.d(TAG, "pre: ")
        when (playList.size) {
            0 -> {
                return
            }
            1 -> {}
            else -> {
                if (nowPlayingPosition == 0) {
                    nowPlayingPosition = playList.size - 1
                } else {
                    nowPlayingPosition--
                }
            }
        }
        resetMediaPlayer()
    }

    override fun seekTo(progress: Int) {
        if (mMediaPlayer.duration != 0) {
            val playerSeek = mMediaPlayer.duration * progress / 1000
            mMediaPlayer.seekTo(playerSeek)
        }
    }

    override fun resetMediaPlayer() {
        stopTimer()
        mMediaPlayer.reset()
        val cid = playList[nowPlayingPosition].copyrightId
        if (cid.isNotEmpty()) {
            val url = "${Constants.BASE_URL}${Constants.SONG}?cid=$cid&br=2"
            Log.d(MainModel.TAG, "loadSong: $url")
            val request: Request = Request.Builder()
                .url(url)
                .get()
                .build()
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(MainModel.TAG, "onFailure: ")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        val json = response.body!!.string()
                        val song = Gson().fromJson(json, Song::class.java)
                        if (song != null && song.data != null && song.data.playUrl != null) {
                            mMediaPlayer.setDataSource(song.data.playUrl)
                            mMediaPlayer.prepareAsync()
//                            mMediaPlayer.prepare()
                        }
                    }
                }
            })
        }
    }

    override fun setNowPlayingPosition(position: Int) {
        nowPlayingPosition = position
    }

    override fun setList(list: ArrayList<SearchSong.MusicsDTO>) {
        playList.clear()
        playList.addAll(list)
    }

    override fun getList(): ArrayList<SearchSong.MusicsDTO> {
        return playList
    }

    private fun startTimer() {
        timer = Timer()
        if (mTimerTask == null) {
            mTimerTask = SeekTimeTask()
        }
        timer!!.schedule(mTimerTask, 0, 200)
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
        mTimerTask?.cancel()
        mTimerTask = null
    }

    private class SeekTimeTask : TimerTask() {
        override fun run() {
            if (mMediaPlayer.currentPosition != 0) {
                val durationPosition = 1000 * mMediaPlayer.currentPosition / mMediaPlayer.duration
                Log.d(TAG, "durationPosition: $durationPosition")
                mViewControl?.onSeekChange(durationPosition, mMediaPlayer.currentPosition)
            }
        }

    }

}