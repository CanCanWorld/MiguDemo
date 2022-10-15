package com.zrq.migudemo.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.zrq.migudemo.R
import com.zrq.migudemo.bean.Cate
import com.zrq.migudemo.bean.Category
import com.zrq.migudemo.databinding.DialogSingleBackgroundBinding
import com.zrq.migudemo.databinding.DialogThemeBinding
import com.zrq.migudemo.util.Constants.GET_CATEGORY
import com.zrq.migudemo.util.Constants.PIC_BASE_URL
import okhttp3.*
import java.io.IOException

class ThemeDialog(
    context: Context,
    private val activity: Activity,
) : Dialog(context, R.style.SingleDialog), View.OnClickListener {

    private val list = ArrayList<Cate>()
    private lateinit var mBinding: DialogThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        mBinding = DialogThemeBinding.inflate(LayoutInflater.from(context))
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

        initEvent()

    }

    private fun initEvent() {
        mBinding.apply {
        }
    }

    companion object {
        const val TAG = "BackgroundDialog"
    }
}