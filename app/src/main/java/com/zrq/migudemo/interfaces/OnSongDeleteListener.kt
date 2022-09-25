package com.zrq.migudemo.interfaces

import com.zrq.migudemo.bean.SearchSong

interface OnSongDeleteListener {
    fun onSongDelete(song: SearchSong.MusicsDTO)
}