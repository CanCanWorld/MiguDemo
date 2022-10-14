package com.zrq.migudemo.helper

import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.media.audiofx.Visualizer.OnDataCaptureListener
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
import kotlin.math.abs
import kotlin.math.hypot

object PlayerHelper : Binder(), IPlayerControl {

    private var mViewControl: IPlayerViewControl? = null

    private var mMediaPlayer: MediaPlayer = MediaPlayer()

    private var playList = ArrayList<SearchSong.MusicsDTO>()

    private var nowPlayingPosition = 0

    private const val TAG = "PlayerHelper"

    private var mTimerTask: SeekTimeTask? = null

    private var timer: Timer? = null

    private var visualizer: Visualizer? = null

    init {
        mMediaPlayer.setOnCompletionListener {
            Log.d(TAG, "setOnCompletionListener: ")
            next()
            mViewControl?.onSongPlayOver(nowPlayingPosition)
            Log.d(TAG, "nowPlayingPosition: $nowPlayingPosition")
        }
        // 因为直接切歌会发生错误，所以增加错误监听器。返回true。就不会回调onCompletion方法了。
        mMediaPlayer.setOnErrorListener { _, _, _ -> true }

        initVisualizer()
    }

    private fun initVisualizer() {
        visualizer?.release()
        visualizer = Visualizer(mMediaPlayer.audioSessionId)
        visualizer!!.setDataCaptureListener(object : OnDataCaptureListener {
            override fun onWaveFormDataCapture(
                visualizer: Visualizer,
                bytes: ByteArray,
                samplingRate: Int
            ) {
            }

            override fun onFftDataCapture(
                visualizer: Visualizer,
                fft: ByteArray,
                samplingRate: Int
            ) {
                val model = FloatArray(fft.size / 2 + 1)
                model[0] = abs(fft[1].toFloat())
                var j = 1
                var i = 2
                while (i < fft.size / 2) {
                    model[j] = hypot(fft[i].toDouble(), fft[i + 1].toDouble()).toFloat()
                    i += 2
                    j++
                    model[j] = abs(fft[j].toFloat())
                }
                //model即为最终用于绘制的数据
                mViewControl?.onVisualizeView(model)
            }
        }, Visualizer.getMaxCaptureRate() / 2, false, true)

        visualizer!!.enabled = true

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
        }
        mMediaPlayer.start()
        startTimer()
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
        play()
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
        play()
    }

    override fun seekTo(seek: Int) {
        if (mMediaPlayer.duration != 0) {
            val playerSeek = mMediaPlayer.duration * seek / 1000
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
                            mMediaPlayer.reset()
                            mMediaPlayer.setDataSource(song.data.playUrl)
                            mMediaPlayer.prepareAsync()
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