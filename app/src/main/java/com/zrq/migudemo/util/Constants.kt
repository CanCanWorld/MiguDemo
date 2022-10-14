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

    //page
    const val PAGE_SEARCH = 1

    const val PAGE_LOVE = 2

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

    //壁纸
    const val PIC_BASE_URL = "http://service.picasso.adesk.com"

    private const val GET_PIC = "/v1/vertical/category/"

    const val GET_CATEGORY = "/v1/vertical/category?adult=false&first=1"

    const val ANIMATION = "4e4d610cdf714d2966000003"

    const val ANIMAL = "4e4d610cdf714d2966000001"

    const val GIRL = "4e4d610cdf714d2966000000"

    fun getPicByCategory(category: String, limit: Int, num: Int): String {
        return "$PIC_BASE_URL$GET_PIC$category/vertical?limit=$limit&skip=${num * limit}&adult=false&first=0&order=hot\""
    }
}