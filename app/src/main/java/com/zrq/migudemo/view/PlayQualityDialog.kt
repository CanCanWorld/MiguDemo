package com.zrq.migudemo.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.tencent.mmkv.MMKV
import com.zrq.migudemo.R
import com.zrq.migudemo.bean.Cate
import com.zrq.migudemo.databinding.DialogPlayQualityBinding
import com.zrq.migudemo.util.Constants
import com.zrq.migudemo.util.Constants.QUALITY_BETTER
import com.zrq.migudemo.util.Constants.QUALITY_HIGH
import com.zrq.migudemo.util.Constants.QUALITY_NORMAL

class PlayQualityDialog(
    context: Context,
    private val activity: Activity,
) : Dialog(context, R.style.SingleDialog) {

    private lateinit var mBinding: DialogPlayQualityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        mBinding = DialogPlayQualityBinding.inflate(LayoutInflater.from(context))
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

    private fun initEvent() {
        mBinding.apply {
            rbNormal.setOnClickListener {
                MMKV.defaultMMKV().encode("quality", QUALITY_NORMAL)
            }
            rbBetter.setOnClickListener {
                MMKV.defaultMMKV().encode("quality", QUALITY_BETTER)
            }
            rbHigh.setOnClickListener {
                MMKV.defaultMMKV().encode("quality", QUALITY_HIGH)
            }
        }
    }

    override fun show() {
        super.show()
        when (MMKV.defaultMMKV().decodeInt("quality", QUALITY_BETTER)) {
            QUALITY_NORMAL -> {
                mBinding.rbNormal.isChecked = true
            }
            QUALITY_BETTER -> {
                mBinding.rbBetter.isChecked = true
            }
            QUALITY_HIGH -> {
                mBinding.rbHigh.isChecked = true
            }
            else -> {}
        }
    }

    companion object {
        const val TAG = "QualityDialog"
    }
}