package com.zrq.migudemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.zrq.migudemo.R
import com.zrq.migudemo.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(){
    override fun providedViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        mBinding.viewPager2.adapter
    }

    override fun initEvent() {
        mBinding.apply {
            rbLove.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Navigation.findNavController(requireActivity(), R.id.fragment_home_container)
                        .navigate(R.id.action_global_loveFragment)
                    rbSearch.isChecked = false
                }
            }

            rbSearch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Navigation.findNavController(requireActivity(), R.id.fragment_home_container)
                        .navigate(R.id.action_global_searchFragment)
                    rbLove.isChecked = false
                }
            }

        }
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}