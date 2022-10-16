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
import com.zrq.migudemo.databinding.DialogThemeBinding

class ThemeDialog(
    context: Context,
    private val activity: Activity,
) : Dialog(context, R.style.SingleDialog) {

    private val list = ArrayList<Cate>()
    private lateinit var mBinding: DialogThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        mBinding = DialogThemeBinding.inflate(LayoutInflater.from(context))
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
            rbPink.setOnClickListener {
                MMKV.defaultMMKV().encode("theme", R.color.pink)
            }
            rbRed.setOnClickListener {
                MMKV.defaultMMKV().encode("theme", R.color.red)
            }
            rbPurple.setOnClickListener {
                MMKV.defaultMMKV().encode("theme", R.color.purple_200)
            }
            rbTeal.setOnClickListener {
                MMKV.defaultMMKV().encode("theme", R.color.teal_200)
            }
            rbGrey.setOnClickListener {
                MMKV.defaultMMKV().encode("theme", R.color.grey)
            }
            rbBlank.setOnClickListener {
                MMKV.defaultMMKV().encode("theme", R.color.black)
            }
        }
    }

    override fun show() {
        super.show()
        when (MMKV.defaultMMKV().decodeInt("theme", R.color.pink)) {
            R.color.pink -> {
                mBinding.rbPink.isChecked = true
            }
            R.color.red -> {
                mBinding.rbRed.isChecked = true
            }
            R.color.purple_200 -> {
                mBinding.rbPurple.isChecked = true
            }
            R.color.teal_200 -> {
                mBinding.rbTeal.isChecked = true
            }
            R.color.grey -> {
                mBinding.rbGrey.isChecked = true
            }
            R.color.black -> {
                mBinding.rbBlank.isChecked = true
            }
            else -> {}
        }
    }

    companion object {
        const val TAG = "ThemeDialog"
    }
}