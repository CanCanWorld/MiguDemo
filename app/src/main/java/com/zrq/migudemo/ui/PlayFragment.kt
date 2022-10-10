//package com.zrq.migudemo.ui
//
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.SeekBar
//import androidx.navigation.Navigation
//import com.google.gson.Gson
//import com.zrq.migudemo.R
//import com.zrq.migudemo.bean.Lyric
//import com.zrq.migudemo.bean.SearchSong
//import com.zrq.migudemo.databinding.FragmentPlayBinding
//import com.zrq.migudemo.interfaces.IPlayerControl
//import com.zrq.migudemo.interfaces.IPlayerViewControl
//import com.zrq.migudemo.util.Constants.BASE_URL
//import com.zrq.migudemo.util.Constants.LYRIC
//import com.zrq.migudemo.util.Utils
//import okhttp3.*
//import java.io.IOException
//
//class PlayFragment : BaseFragment<FragmentPlayBinding>(), IPlayerViewControl {
//    override fun providedViewBinding(
//        inflater: LayoutInflater,
//        container: ViewGroup?
//    ): FragmentPlayBinding {
//        return FragmentPlayBinding.inflate(inflater, container, false)
//    }
//
//    private lateinit var nowPlaying: SearchSong.MusicsDTO
//
////    private lateinit var mPlayerViewControl: IPlayerViewControl
//
//    override fun initData() {
////        if (mainModel.playSong != null) {
////            nowPlaying = mainModel.playSong!!
////            refresh()
////        }
//    }
//
//    override fun initEvent() {
//
////        mPlayerViewControl = this
////
////        mainModel.playerControl?.registerViewControl(mPlayerViewControl)
//
//        mBinding.apply {
//            ibBack.setOnClickListener {
//                Navigation.findNavController(requireActivity(), R.id.fragment_container)
//                    .popBackStack()
//            }
//
//            cbPlay.setOnCheckedChangeListener { _, isChecked ->
//                if (isChecked) {
//                    mBinding.cbPlay.setButtonDrawable(R.drawable.ic_baseline_pause_circle_outline_24)
//                    mainModel.start()
//                } else {
//                    mBinding.cbPlay.setButtonDrawable(R.drawable.ic_baseline_play_circle_outline_24)
//                    mainModel.pause()
//                }
//            }
//
//            cbPre.setOnCheckedChangeListener { _, _ ->
//                Log.d(TAG, "cbPre: ")
//                mainModel.pre()
//            }
//
//            cbNext.setOnCheckedChangeListener { _, _ ->
//                Log.d(TAG, "cbNext: ")
//                mainModel.next()
//            }
//
//            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                override fun onProgressChanged(
//                    seekBar: SeekBar?,
//                    progress: Int,
//                    fromUser: Boolean
//                ) {
//                    if (fromUser) {
//                        Log.d(TAG, "onProgressChanged: $progress")
//                        mainModel.playerControl?.seekTo(progress)
//                    }
//                }
//
//                override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                }
//
//                override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                }
//            })
//        }
//
//        mainModel.duration.observe(this) {
//            if (it != null) {
//                mBinding.tvPlayEnd.text = Utils.formatDuration(it)
//            }
//        }
//
//        mainModel.isPause.observe(this) {
//            if (it != null) {
//                mBinding.cbPlay.isChecked = !it
//            }
//        }
//    }
//
//    private fun refresh() {
//        mBinding.apply {
//            tvSongName.text = nowPlaying.songName
//            tvSinger.text = nowPlaying.singerName
//        }
//        loadLyric()
//    }
//
//    private fun loadLyric() {
//        val cid = nowPlaying.copyrightId
//        if (cid.isNotEmpty()) {
//            val url = "${BASE_URL}${LYRIC}?cid=$cid"
//            Log.d(TAG, "loadLyric: $url")
//            val request: Request = Request.Builder()
//                .url(url)
//                .get()
//                .build()
//            OkHttpClient().newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    Log.d(TAG, "onFailure: ")
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.body != null) {
//                        val json = response.body!!.string()
//                        val lyric = Gson().fromJson(json, Lyric::class.java)
//                        if (lyric != null && lyric.lyric != null) {
//                            requireActivity().runOnUiThread {
//                                mBinding.tvLyric.text = lyric.lyric
//                            }
//                        }
//                    }
//                }
//            })
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        mBinding.cbPlay.isChecked = true
//    }
//
//    override fun onSeekChange(progress: Int, elapsedTime: Int) {
//        Handler(Looper.getMainLooper()).post(Runnable {
//            Log.d(TAG, "onSeekChange: $progress")
//            mBinding.seekBar.progress = progress
//            mBinding.tvPlayStart.text = Utils.formatDuration(elapsedTime)
//        })
//    }
//
//    override fun onSongPlayOver(position: Int) {
//        Log.d(TAG, "onSongPlayOver: 2")
//    }
//
////    override fun onSongChange(duration: Int) {
////        Handler(Looper.getMainLooper()).post(Runnable {
////            mBinding.tvPlayEnd.text = Utils.formatDuration(duration)
////        })
////    }
//
//    companion object {
//        const val TAG = "PlayFragment"
//    }
//
//}
