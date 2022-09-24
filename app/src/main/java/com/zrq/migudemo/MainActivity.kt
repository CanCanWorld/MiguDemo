package com.zrq.migudemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zrq.migudemo.util.StatusBarUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtil.transparencyBar(this)
    }
}