package com.zrq.migudemo.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.zrq.migudemo.R
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.databinding.FragmentPlayBarBinding
import com.zrq.migudemo.interfaces.IPlayerViewControl
import com.zrq.migudemo.view.VisualizeView.CIRCLE

class PlayBarFragment
    (var position: Int) : BaseFragment<FragmentPlayBarBinding>() {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayBarBinding {
        return FragmentPlayBarBinding.inflate(inflater, container, false)
    }

    private lateinit var animation: ValueAnimator
    private lateinit var mPlayerViewControl: IPlayerViewControl

    override fun initData() {
        initAnimation()
    }

    override fun initEvent() {
        mBinding.apply {

            llPlayBar.setOnClickListener {
//                Navigation.findNavController(requireActivity(), R.id.fragment_container)
//                    .navigate(R.id.playFragment)
            }

        }

        mainModel.apply {
            nowPlaying.observe(this@PlayBarFragment) {
                if (it != null) {
                    refreshPlayBar(it)
                }
            }
        }

    }

    private fun initAnimation() {
        animation = ObjectAnimator.ofFloat(mBinding.ivAlbum, "rotation", .0f, 360.0f)
        animation.apply {
            duration = 6000
            interpolator = LinearInterpolator()
            repeatCount = -1
            repeatMode = ObjectAnimator.RESTART
        }
    }

    private fun refreshPlayBar(song: SearchSong.MusicsDTO) {
        mBinding.apply {
            llPlayBar.visibility = View.VISIBLE
            ivAlbum.visibility = View.VISIBLE
            tvSongName.text = song.songName
            tvSinger.text = song.singerName
            Glide.with(this@PlayBarFragment)
                .load(song.cover)
                .into(ivAlbum)
            animation.start()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val TAG = "PlayBarFragment"

        fun getInstance(position: Int) = PlayBarFragment(position)
    }

}
