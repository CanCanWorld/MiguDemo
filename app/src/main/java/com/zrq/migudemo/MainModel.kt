package com.zrq.migudemo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.interfaces.IPlayerControl
import com.zrq.migudemo.interfaces.IPlayerViewControl
import com.zrq.migudemo.interfaces.OnSongChangeListener

class MainModel : ViewModel() {

    var nowPlaying = MutableLiveData<SearchSong.MusicsDTO>()

    val key = MutableLiveData<String>()

    val artistId = MutableLiveData<String>()

    val duration = MutableLiveData<Int>()

    val isPause = MutableLiveData<Boolean>()

    var onSongChangeListener: OnSongChangeListener? = null

    var playerControl: IPlayerControl? = null

    fun getList(): ArrayList<SearchSong.MusicsDTO>? {
        return playerControl?.getList()
    }

    fun setList(list: ArrayList<SearchSong.MusicsDTO>) {
        playerControl?.setList(list)
    }

    fun registerViewControl(iPlayerViewControl: IPlayerViewControl) {
        Thread {
            for (i in 1..10)
                if (playerControl != null) {
                    playerControl?.registerViewControl(iPlayerViewControl)
                    break
                } else {
                    Thread.sleep(200)
                }
        }.start()

    }

    fun playThis(position: Int) {
        playerControl?.setNowPlayingPosition(position)
        playerControl?.resetMediaPlayer()
        playerControl?.play()
    }

    fun start() {
        playerControl?.play()
        isPause.postValue(false)
    }

    fun pause() {
        playerControl?.pause()
        isPause.postValue(true)
    }

    fun next() {
        playerControl?.next()
    }

    fun pre() {
        playerControl?.pre()
    }

    fun destroy() {
        isPause.postValue(true)
    }

    companion object {
        const val TAG = "MainModel"
    }

}