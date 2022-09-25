package com.zrq.migudemo.interfaces

import com.zrq.migudemo.bean.SearchSong

interface OnSongMoreClickListener {
    fun onSongMoreClick(song: SearchSong.MusicsDTO, position:Int)
}