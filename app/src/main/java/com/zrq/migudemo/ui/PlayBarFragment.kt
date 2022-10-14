package com.zrq.migudemo.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.bumptech.glide.Glide
import com.zrq.migudemo.databinding.FragmentPlayBarBinding

class PlayBarFragment
    (var position: Int) : BaseFragment<FragmentPlayBarBinding>() {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayBarBinding {
        return FragmentPlayBarBinding.inflate(inflater, container, false)
    }

    private lateinit var animation: ValueAnimator

    override fun initData() {
        initAnimation()
        refreshPlayBar()
    }

    override fun initEvent() {
        mBinding.apply {
            llPlayBar.setOnClickListener {
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

    private fun refreshPlayBar() {
        val song = mainModel.getList()?.get(position)
        if (song != null) {
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
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: $position")
    }

    companion object {
        const val TAG = "PlayBarFragment"

        fun getInstance(position: Int) = PlayBarFragment(position)
    }

}
