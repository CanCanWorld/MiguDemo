package com.zrq.migudemo.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.tencent.mmkv.MMKV
import com.zrq.migudemo.bean.DownloadSong
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val TAG = "Utils"
    private val handler = Handler(Looper.getMainLooper())

    fun formatDuration(duration: Int): String {
        if (duration == 0) return "00:00"
        val m = duration / (60 * 1000)
        val s = (duration - 60 * 1000 * m) / 1000
        return String.format("%02d", m) + ":" + String.format("%02d", s)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun download(ctx: Context, song: DownloadSong, btn: Button) {
        Thread {
            val path = song.path
            var fileOps: FileOutputStream? = null
            var inputStream: InputStream? = null
            try {
                val url = URL(path)
                val connect: HttpURLConnection = url.openConnection() as HttpURLConnection
                connect.doInput = true
                connect.connect()
                inputStream = connect.inputStream

                val defaultPath =
                    ctx.getExternalFilesDir(null)!!.absolutePath + File.separator + "download"
                val filePath =
                    MMKV.defaultMMKV().decodeString("path", defaultPath)

                if (filePath != null) {
                    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
                    val fileName = sdf.format(Date().time) + song.name + ".mp3"
                    val dir = File(filePath)
                    if (!dir.exists()) {
                        dir.mkdir()
                    }
                    Log.d(TAG, "filePath: $filePath")
                    val fileLength = connect.contentLengthLong
                    Log.d(TAG, "fileLength: $fileLength")
                    val file = File(dir, fileName)
                    fileOps = FileOutputStream(file)
                    val buffer = ByteArray(1024)
                    var ch: Int
                    var i = 0
                    var progress = 0

                    while (inputStream.read(buffer).also { ch = it } != -1) {
                        fileOps.write(buffer, 0, ch)
                        i++
                        if (i * 1024 < fileLength) {
                            if (progress < i * 1024f / fileLength * 100) {
                                progress++
                                handler.post {
                                    btn.text = "$progress%"
                                }
                            }
                        }
                    }
                    handler.post {
                        btn.text = "已下载"
                        btn.isEnabled = false
                        Toast.makeText(ctx, "下载成功^_^: ${song.name}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    handler.post {
                        Toast.makeText(ctx, "下载失败: ${song.name},路径为空", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "download: $e")
                handler.post {
                    Toast.makeText(ctx, "下载失败111: ${song.name}, 网络有问题", Toast.LENGTH_SHORT).show()
                }
            } finally {
                inputStream?.close()
                fileOps?.flush()
                fileOps?.close()
            }
        }.start()
    }

}