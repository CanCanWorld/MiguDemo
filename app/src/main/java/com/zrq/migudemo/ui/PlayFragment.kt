package com.zrq.migudemo.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.zrq.migudemo.databinding.FragmentPlayBinding

class PlayFragment : BaseFragment<FragmentPlayBinding>() {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayBinding {
        return FragmentPlayBinding.inflate(inflater, container, false)
    }
    private lateinit var animation: ValueAnimator

    override fun initData() {
        animation = ObjectAnimator.ofFloat(mBinding.ivAlbum, "rotation", .0f, 360.0f)
        animation.apply {
            duration = 6000
            interpolator = LinearInterpolator()
            repeatCount = -1
            repeatMode = ObjectAnimator.RESTART
        }
    }

    override fun initEvent() {
    }

}
