package com.zrq.migudemo

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.Song

class MainModel : ViewModel() {
    val nowPlaying = MutableLiveData<SearchSong.MusicsDTO>()

    lateinit var nowSong: Song

    private lateinit var mediaPlayer: MediaPlayer


    fun play() {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(nowSong.data.playUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
    }

    fun pause() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}