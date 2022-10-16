package com.zrq.migudemo.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tencent.mmkv.MMKV
import com.zrq.migudemo.R
import com.zrq.migudemo.adapter.PathAdapter
import com.zrq.migudemo.databinding.DialogPathBinding
import com.zrq.migudemo.interfaces.OnItemClickListener
import java.io.File

class PathDialog(
    context: Context,
    private val activity: Activity,
) : Dialog(context, R.style.SingleDialogNoTitle) {

    private lateinit var mBinding: DialogPathBinding
    private lateinit var adapter: PathAdapter
    private val list = ArrayList<String>()
    private var mDir = BASE_PATH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        mBinding = DialogPathBinding.inflate(LayoutInflater.from(context))
        initData()
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

    private fun initData() {
        adapter = PathAdapter(context, list,
            object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    listFiles(list[position])
                }
            },
            object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val path = list[position]
                    mBinding.tvPath.text = path
                    MMKV.defaultMMKV().encode("path", path)
                }
            })
        mBinding.rvPath.adapter = adapter
        mBinding.rvPath.layoutManager = LinearLayoutManager(context)
        listFiles(mDir)
    }

    private fun initEvent() {
        mBinding.btnBack.setOnClickListener {
            if (File(mDir).parent != null && mDir != BASE_PATH) {
                listFiles(File(mDir).parent!!)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listFiles(path: String) {
        Log.d(TAG, "path: $path")
        list.clear()
        mDir = path
        val dir = File(path)
        if (dir.isDirectory) {
            val files = dir.listFiles()
            if (files != null) {
                for (file in files) {
                    Log.d(TAG, "listFiles: ${file.absolutePath}")
                    list.add(file.absolutePath)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun show() {
        super.show()
        val defaultPath = context.getExternalFilesDir(null)!!.absolutePath + File.separator + "download"
        val path = MMKV.defaultMMKV().decodeString("path", defaultPath)
        mBinding.tvPath.text = path
    }

    companion object {
        const val TAG = "PathDialog"
        const val BASE_PATH = "/storage/emulated/0"
    }

}