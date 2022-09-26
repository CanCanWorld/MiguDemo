package com.zrq.migudemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zrq.migudemo.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initData() {
    }

    override fun initEvent() {
    }

  }