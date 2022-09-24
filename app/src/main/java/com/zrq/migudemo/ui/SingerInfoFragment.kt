package com.zrq.migudemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.Gson
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.FragmentSingerInfoBinding
import com.zrq.migudemo.util.Constants
import com.zrq.migudemo.util.Constants.BASE_URL
import com.zrq.migudemo.util.Constants.SINGER_INFO
import okhttp3.*
import java.io.IOException

class SingerInfoFragment : BaseFragment<FragmentSingerInfoBinding>() {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSingerInfoBinding {
        return FragmentSingerInfoBinding.inflate(inflater, container, false)
    }

    override fun initData() {
    }

    override fun initEvent() {
        mainModel.artistId.observe(this) {
            if (it != null) {
                loadSinger()
            }
        }
    }

    private fun loadSinger() {
        val url = "$BASE_URL$SINGER_INFO?key=$key"
        Log.d(TAG, "initEvent: $url")
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: ")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call, response: Response) {
                if (response.body != null) {
                    val json = response.body!!.string()
                    Log.d(TAG, "onResponse: $json")
                    val song = Gson().fromJson(json, SearchSong::class.java)
                    if (song != null && song.musics != null) {
                        listSong.clear()
                        listSong.addAll(song.musics)
                        requireActivity().runOnUiThread {
                            songAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "SingerInfoFragment"
    }
}