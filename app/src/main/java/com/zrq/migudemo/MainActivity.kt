package com.zrq.migudemo

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.lifecycle.ViewModelProvider
import com.zrq.migudemo.interfaces.IPlayerControl
import com.zrq.migudemo.service.PlayerService
import com.zrq.migudemo.util.StatusBarUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtil.transparencyBar(this)
        mainModel = ViewModelProvider(this).get(MainModel::class.java)
        initService()
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
}