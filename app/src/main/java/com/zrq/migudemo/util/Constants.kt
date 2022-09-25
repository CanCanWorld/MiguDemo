package com.zrq.migudemo.util

object Constants {
    const val BASE_URL = "http://iecoxe.top:5000/v1/migu"

    const val SEARCH = "/search"

    const val SONG = "/song"

    const val SINGER_INFO = "/singer/info"

    const val SINGER_SONG_LIST = "/singer/songList"

    const val HOT_SEARCH = "/hotSearch"

    const val SUGGEST_SEARCH = "/suggestSearch"

    const val LYRIC = "/lyric"

    const val TYPE_SINGER = 1
    const val TYPE_SONG = 2
    const val TYPE_ALBUM = 4
    const val TYPE_MV = 5
    const val TYPE_SONG_LIST = 6
    const val TYPE_LYRICS = 7

    //数据库
    const val DATABASE_NAME = "nice_music"

    const val DATABASE_VERSION = 1

    const val TABLE_NAME = "nice_music_table"

    const val FIELD_ID = "_id"

    const val FIELD_SONG_NAME = "song_name"

    const val FIELD_SINGER = "singer"

    const val FIELD_ALBUM = "album"

    const val FIELD_COVER = "cover"

    const val FIELD_CID = "cid"
}