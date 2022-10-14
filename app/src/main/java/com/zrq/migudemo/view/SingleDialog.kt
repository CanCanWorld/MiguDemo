package com.zrq.migudemo.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.zrq.migudemo.R
import com.zrq.migudemo.interfaces.OnDialogItemClickListener

class SingleDialog(
    context: Context,
    private val activity: Activity,
    private val layoutResId: Int,
    private var listenerArray: ArrayList<Int>? = null,
    private var onDialogItemClickListener: OnDialogItemClickListener
) : Dialog(context, R.style.SingleDialog), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        setContentView(layoutResId)

        val display = activity.windowManager.defaultDisplay
        if (window != null) {
            val lp = window?.attributes
            if (lp != null) {
                lp.width = display.width * 4 / 5
                window!!.attributes = lp
                setCanceledOnTouchOutside(true)
                if (listenerArray != null) {
                    for (id in listenerArray!!) {
                        findViewById<View>(id).setOnClickListener(this)
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            onDialogItemClickListener.onDialogItemClick(v)
        }
    }
}