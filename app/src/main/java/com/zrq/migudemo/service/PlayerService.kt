package com.zrq.migudemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.zrq.migudemo.helper.PlayerHelper

class PlayerService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return PlayerHelper
    }
}