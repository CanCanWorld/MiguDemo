package com.zrq.migudemo.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.zrq.migudemo.R
import com.zrq.migudemo.bean.Cate
import com.zrq.migudemo.bean.DownloadSong
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.bean.Song
import com.zrq.migudemo.databinding.DialogDownloadBinding
import com.zrq.migudemo.util.Constants.BASE_URL
import com.zrq.migudemo.util.Constants.QUALITY_BETTER
import com.zrq.migudemo.util.Constants.QUALITY_HIGH
import com.zrq.migudemo.util.Constants.QUALITY_NORMAL
import com.zrq.migudemo.util.Constants.SONG
import com.zrq.migudemo.util.Utils
import okhttp3.*
import java.io.IOException

class DownloadDialog(
    context: Context,
    private val activity: Activity
) : Dialog(context, R.style.SingleDialog) {

    private lateinit var mBinding: DialogDownloadBinding
    var downloadSong: SearchSong.MusicsDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        mBinding = DialogDownloadBinding.inflate(LayoutInflater.from(context))
        initEvent()
        setContentView(mBinding.root)

        val display = activity.windowManager.defaultDisplay
        if (window != null) {
            val lp = window?.attributes
            if (lp != null) {
                lp.width = display.width * 4 / 5
                window!!.attributes = lp
                setCanceledOnTouchOutside(true)
            }
        }

    }

    override fun show() {
        super.show()
        initBtn()
    }

    @SuppressLint("SetTextI18n")
    private fun initBtn() {
        mBinding.apply {
            btnNormal.isEnabled = true
            btnNormal.text = "普通"
            btnBetter.isEnabled = true
            btnBetter.text = "高品质"
            btnHigh.isEnabled = true
            btnHigh.text = "无损(VIP)"
        }
    }

    private fun initEvent() {
        mBinding.apply {
            btnNormal.setOnClickListener {
                loadSongPath(QUALITY_NORMAL, btnNormal)
            }
            btnBetter.setOnClickListener {
                loadSongPath(QUALITY_BETTER, btnBetter)
            }
            btnHigh.setOnClickListener {
                loadSongPath(QUALITY_HIGH, btnHigh)
            }
        }
    }


    private fun loadSongPath(quality: Int, btn: Button) {
        if (downloadSong != null) {
            val cid = downloadSong!!.copyrightId
            if (cid.isNotEmpty()) {
                val url = "$BASE_URL$SONG?cid=$cid&br=$quality"
                Log.d(TAG, "loadSongPath: $url")
                val request: Request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
                OkHttpClient().newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d(TAG, "onFailure: loadSongPath")
                    }

                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onResponse(call: Call, response: Response) {
                        if (response.body != null) {
                            val json = response.body!!.string()
                            val song = Gson().fromJson(json, Song::class.java)
                            if (song != null && song.data != null && song.data.playUrl != null) {
                                Utils.download(
                                    context,
                                    DownloadSong(downloadSong!!.songName, song.data.playUrl),
                                    btn
                                )
                            }
                        }
                    }
                })
            }
        }

    }

    companion object {
        const val TAG = "DownloadDialog"
    }
}