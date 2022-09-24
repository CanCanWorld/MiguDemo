package com.zrq.migudemo

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.Song
import java.io.IOException

class MainModel : ViewModel() {
    val nowPlaying = MutableLiveData<SearchSong.MusicsDTO>()

    val key = MutableLiveData<String>()

    val artistId = MutableLiveData<String>()

    lateinit var nowSong: Song

    private var mediaPlayer = MediaPlayer()

    fun play() {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(nowSong.data.playUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun pause() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}