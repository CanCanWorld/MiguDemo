package com.zrq.migudemo.bean

import androidx.annotation.Keep

@Keep
data class LyricByTime(var time: Int, var lyric: String)

@Keep
data class DownloadSong(var name: String, var path: String)
