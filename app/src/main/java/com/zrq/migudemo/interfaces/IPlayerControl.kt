package com.zrq.migudemo.interfaces

import com.zrq.migudemo.bean.SearchSong

interface IPlayerControl {

    fun registerViewControl(viewControl: IPlayerViewControl)

    fun unRegisterViewControl()

    fun resetMediaPlayer()

    fun play()

    fun start()

    fun pause()

    fun next()

    fun pre()

    fun seekTo(seek: Int)

    fun setNowPlayingPosition(position: Int)

    fun setList(list: ArrayList<SearchSong.MusicsDTO>)

    fun getList(): ArrayList<SearchSong.MusicsDTO>
}