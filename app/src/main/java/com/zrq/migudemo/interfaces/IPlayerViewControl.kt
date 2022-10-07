package com.zrq.migudemo.interfaces

interface IPlayerViewControl {

    fun onSeekChange(progress: Int, elapsedTime: Int)

    fun onSongChange(duration: Int)

}