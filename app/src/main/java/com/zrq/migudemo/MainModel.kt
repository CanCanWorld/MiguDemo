package com.zrq.migudemo

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.Song
import com.zrq.migudemo.interfaces.OnElapsedTimeListener
import com.zrq.migudemo.interfaces.OnSongChangeListener
import com.zrq.migudemo.util.Constants.BASE_URL
import com.zrq.migudemo.util.Constants.SONG
import okhttp3.*
import java.io.IOException

class MainModel : ViewModel() {

    var playSong: SearchSong.MusicsDTO? = null

    var nowPlaying = MutableLiveData<SearchSong.MusicsDTO>()

    var playList = ArrayList<SearchSong.MusicsDTO>()

    var playPosition = 0

    val key = MutableLiveData<String>()

    val artistId = MutableLiveData<String>()

    val duration = MutableLiveData<Int>()

    val elapsedTime = MutableLiveData<Int>()

    private var mediaPlayer = MediaPlayer()


    var onSongChangeListener: OnSongChangeListener? = null

    var onElapsedTimeListener: OnElapsedTimeListener? = null

    init {
        mediaPlayer.setOnCompletionListener {
            if (playPosition == playList.size - 1) {
                playPosition = 0
            } else {
                playPosition++
            }
            playThis(playPosition)
            nowPlaying.postValue(playList[playPosition])
        }

    }

    fun playThis(position: Int) {
        if (position >= 0) {
            playPosition = position
            Log.d(TAG, "playThis: $position")
            val cid = playList[position].copyrightId
            if (cid.isNotEmpty()) {
                val url = "${BASE_URL}${SONG}?cid=$cid&br=2"
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
                            val song = Gson().fromJson(json, Song::class.java)
                            if (song != null) {
                                play(song)
                            }
                        }
                    }
                })
            }
        }
    }

    fun play(song: Song) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(song.data.playUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                start()
                duration.postValue(mediaPlayer.duration)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun start() {
        mediaPlayer.start()
        flag = true
        ProgressThread().start()
    }

    fun pause() {
        mediaPlayer.pause()
        flag = false
    }

    fun getDuration(): Int {
        return mediaPlayer.duration
    }

    var flag = true

    inner class ProgressThread : Thread() {
        override fun run() {
            super.run()
            while (flag) {
                if (mediaPlayer.isPlaying) {
                    sleep(100)
                    onElapsedTimeListener?.onElapsedTime(mediaPlayer.currentPosition)
                }
            }
        }
    }

    companion object {
        const val TAG = "MainModel"
    }
}