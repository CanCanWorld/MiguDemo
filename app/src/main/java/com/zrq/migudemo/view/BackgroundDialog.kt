package com.zrq.migudemo.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RadioButton
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.zrq.migudemo.R
import com.zrq.migudemo.bean.Cate
import com.zrq.migudemo.bean.Category
import com.zrq.migudemo.databinding.DialogSingleBackgroundBinding
import com.zrq.migudemo.util.Constants.GET_CATEGORY
import com.zrq.migudemo.util.Constants.PIC_BASE_URL
import okhttp3.*
import java.io.IOException

class BackgroundDialog(
    context: Context,
    private val activity: Activity,
) : Dialog(context, R.style.SingleDialog){

    private val list = ArrayList<Cate>()
    private lateinit var mBinding: DialogSingleBackgroundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        mBinding = DialogSingleBackgroundBinding.inflate(LayoutInflater.from(context))
        setContentView(mBinding.root)

        load()

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

    private fun load() {
        val url = PIC_BASE_URL + GET_CATEGORY
        val request: Request = Request.Builder()
            .url(url)
            .method("GET", null)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: ")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    val string = response.body?.string()
                    val result = Gson().fromJson(string, Category::class.java)
                    list.clear()
                    if (result != null && result.res != null && result.res.category != null) {
                        list.clear()
                        for (i in result.res.category.indices) {
                            val it = result.res.category[i]
                            Log.d(TAG, "onResponse: ${it.name}")
                            list.add(Cate(it.name, it.id, i))
                        }
                        activity.runOnUiThread {
                            list.forEach { it1 ->
                                val rb = RadioButton(context)
                                rb.id = it1.resId
                                rb.text = it1.name
                                rb.setOnClickListener {
                                    MMKV.defaultMMKV().encode("background", it1.id)
                                }
                                mBinding.radioGroup.addView(rb)
                            }
                        }
                    }
                }
            }
        })
    }


    companion object {
        const val TAG = "BackgroundDialog"
    }
}