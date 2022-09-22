package com.zrq.migudemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zrq.migudemo.bean.SearchSong

class MainModel: ViewModel() {
    val nowPlaying = MutableLiveData<SearchSong.MusicsDTO>()
}