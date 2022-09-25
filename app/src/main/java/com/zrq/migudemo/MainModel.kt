package com.zrq.migudemo

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.Song
import com.zrq.migudemo.interfaces.OnSongChangeListener
import com.zrq.migudemo.ui.PlayFragment
import com.zrq.migudemo.util.Constants
import com.zrq.migudemo.util.Constants.BASE_URL
import com.zrq.migudemo.util.Constants.SONG
import okhttp3.*
import java.io.IOException

class MainModel : ViewModel() {

    var playSong : SearchSong.MusicsDTO? = null

    var nowPlaying = MutableLiveData<SearchSong.MusicsDTO>()

    var playList = ArrayList<SearchSong.MusicsDTO>()

    var playPosition = 0

    val key = MutableLiveData<String>()

    val artistId = MutableLiveData<String>()

    private var mediaPlayer = MediaPlayer()

    private lateinit var onSongChangeListener: OnSongChangeListener

    fun getOnSongChangeListener(): OnSongChangeListener {
        return onSongChangeListener
    }

    fun setOnSongChangeListener(onItemClickListener: OnSongChangeListener) {
        this.onSongChangeListener = onItemClickListener
    }


    fun playThis(position: Int) {
        if (position > 0) {
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
                            Log.d(TAG, "onResponse: $json")
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
                mediaPlayer.start()
            }
            mediaPlayer.setOnCompletionListener {
                if (playPosition == playList.size - 1) {
                    playPosition = 0
                } else {
                    playPosition++
                }
                playThis(playPosition)
                nowPlaying.postValue(playList[playPosition])
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun pause() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    companion object {
        const val TAG = "MainModel"
    }
}