package com.zrq.migudemo.interfaces

import com.zrq.migudemo.bean.SearchSong

interface OnSongChangeListener {
    fun onSongChange(song: SearchSong.MusicsDTO)
}