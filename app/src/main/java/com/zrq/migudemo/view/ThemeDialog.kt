package com.zrq.migudemo.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.tencent.mmkv.MMKV
import com.zrq.migudemo.R
import com.zrq.migudemo.bean.Cate
import com.zrq.migudemo.databinding.DialogThemeBinding
import com.zrq.migudemo.interfaces.OnThemeChangeListener

class ThemeDialog(
    context: Context,
    private val activity: Activity,
    private val onThemeChangeListener: OnThemeChangeListener
) : Dialog(context, R.style.SingleDialog) {

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
                MMKV.defaultMMKV().encode("theme_type", "pink")
                onThemeChangeListener.onThemeChange()
            }
            rbRed.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "red")
                onThemeChangeListener.onThemeChange()
            }
            rbPurple.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "purple")
                onThemeChangeListener.onThemeChange()
            }
            rbTeal.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "teal")
                onThemeChangeListener.onThemeChange()
            }
            rbGrey.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "grey")
                onThemeChangeListener.onThemeChange()
            }
            rbBlank.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "black")
                onThemeChangeListener.onThemeChange()
            }
        }
    }

    override fun show() {
        super.show()
        when (MMKV.defaultMMKV().decodeString("theme_type", "pink")) {
            "pink" -> {
                mBinding.rbPink.isChecked = true
            }
            "red" -> {
                mBinding.rbRed.isChecked = true
            }
            "purple" -> {
                mBinding.rbPurple.isChecked = true
            }
            "teal" -> {
                mBinding.rbTeal.isChecked = true
            }
            "grey" -> {
                mBinding.rbGrey.isChecked = true
            }
            "black" -> {
                mBinding.rbBlank.isChecked = true
            }
            else -> {}
        }
    }

    companion object {
        const val TAG = "ThemeDialog"
    }
}