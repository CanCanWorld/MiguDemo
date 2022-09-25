package com.zrq.migudemo.dao

import com.zrq.migudemo.bean.SearchSong

interface ISongDao {
    fun addSong(song: SearchSong.MusicsDTO): Long

    fun deleteSong(id: Int): Int

    fun listAllSongs(): ArrayList<SearchSong.MusicsDTO>
}