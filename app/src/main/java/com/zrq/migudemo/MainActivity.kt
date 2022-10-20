package com.zrq.migudemo

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.tencent.mmkv.MMKV
import com.zrq.migudemo.interfaces.IPlayerControl
import com.zrq.migudemo.service.PlayerService
import com.zrq.migudemo.util.StatusBarUtil


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MMKV.initialize(this)
        setBaseTheme()
        setContentView(R.layout.activity_main)
        requestPermissions()
        StatusBarUtil.transparencyBar(this)
        mainModel = ViewModelProvider(this).get(MainModel::class.java)
        initService()
    }

    private fun setBaseTheme() {
        val theme = MMKV.defaultMMKV().decodeString("theme_type")
        val id: Int
        when (theme) {
            "red" -> {
                id = R.style.Theme_Red
            }
            "pink" -> {
                id = R.style.Theme_Pink
            }
            "purple" -> {
                id = R.style.Theme_Purple
            }
            "teal" -> {
                id = R.style.Theme_Teal
            }
            "gold" -> {
                id = R.style.Theme_Gold
            }
            "grey" -> {
                id = R.style.Theme_Grey
            }
            "black" -> {
                id = R.style.Theme_Black
            }
            else -> {
                id = R.style.Theme_Red
            }
        }
        setTheme(id)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestPermissions() {
        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
        }
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
    }

    private lateinit var mainModel: MainModel

    private fun initService() {
        val intent = Intent(this, PlayerService::class.java)
        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val mPlayerControl: IPlayerControl = service as IPlayerControl
                mainModel.playerControl = mPlayerControl
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }
        startService(intent)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
        )
    }
}