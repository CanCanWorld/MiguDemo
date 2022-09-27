package com.zrq.migudemo.util

object Utils {
    fun formatDuration(duration: Int): String {
        val m = duration / (60 * 1000)
        val s = (duration - 60 * 1000 * m) / 1000
        return String.format("%02d", m) + ":" + String.format("%02d", s)
    }


}