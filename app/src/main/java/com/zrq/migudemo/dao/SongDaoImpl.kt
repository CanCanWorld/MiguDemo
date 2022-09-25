package com.zrq.migudemo.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.zrq.migudemo.bean.SearchSong
import com.zrq.migudemo.db.SongDatabaseHelper
import com.zrq.migudemo.util.Constants.FIELD_ALBUM
import com.zrq.migudemo.util.Constants.FIELD_CID
import com.zrq.migudemo.util.Constants.FIELD_COVER
import com.zrq.migudemo.util.Constants.FIELD_ID
import com.zrq.migudemo.util.Constants.FIELD_SINGER
import com.zrq.migudemo.util.Constants.FIELD_SONG_NAME
import com.zrq.migudemo.util.Constants.TABLE_NAME

class SongDaoImpl(
    private var mHelper: SongDatabaseHelper
) : ISongDao {
    override fun addSong(song: SearchSong.MusicsDTO): Long {
        val db = mHelper.writableDatabase
        val values = songToValues(song)
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    override fun deleteSong(id: Int): Int {
        val db = mHelper.writableDatabase
        val result = db.delete(TABLE_NAME, "$FIELD_ID=$id", null)
        db.close()
        return result
    }

    override fun listAllSongs(): ArrayList<SearchSong.MusicsDTO> {
        val db = mHelper.writableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val pics = ArrayList<SearchSong.MusicsDTO>()
        while (cursor.moveToNext()) {
            pics.add(cursorToSong(cursor))
        }
        return pics
    }

    private fun songToValues(song: SearchSong.MusicsDTO): ContentValues {
        val values = ContentValues()
        values.put(FIELD_SONG_NAME, song.songName)
        values.put(FIELD_SINGER, song.singerName)
        values.put(FIELD_ALBUM, song.albumName)
        values.put(FIELD_COVER, song.cover)
        values.put(FIELD_CID, song.copyrightId)
        return values
    }

    @SuppressLint("Range")
    private fun cursorToSong(cursor: Cursor): SearchSong.MusicsDTO {
        val song = SearchSong.MusicsDTO()
        song.id = cursor.getInt(cursor.getColumnIndex(FIELD_ID)).toString()
        song.songName = cursor.getString(cursor.getColumnIndex(FIELD_SONG_NAME))
        song.singerName = cursor.getString(cursor.getColumnIndex(FIELD_SINGER))
        song.albumName = cursor.getString(cursor.getColumnIndex(FIELD_ALBUM))
        song.cover = cursor.getString(cursor.getColumnIndex(FIELD_COVER))
        song.copyrightId = cursor.getString(cursor.getColumnIndex(FIELD_CID))
        return song
    }
}