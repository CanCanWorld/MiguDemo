package com.zrq.migudemo.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zrq.migudemo.util.Constants.DATABASE_NAME
import com.zrq.migudemo.util.Constants.DATABASE_VERSION
import com.zrq.migudemo.util.Constants.FIELD_ALBUM
import com.zrq.migudemo.util.Constants.FIELD_CID
import com.zrq.migudemo.util.Constants.FIELD_COVER
import com.zrq.migudemo.util.Constants.FIELD_ID
import com.zrq.migudemo.util.Constants.FIELD_SINGER
import com.zrq.migudemo.util.Constants.FIELD_SONG_NAME
import com.zrq.migudemo.util.Constants.TABLE_NAME

class SongDatabaseHelper(var context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table $TABLE_NAME($FIELD_ID integer primary key autoincrement," +
                "$FIELD_SONG_NAME varchar(50)," +
                "$FIELD_SINGER varchar(50)," +
                "$FIELD_ALBUM varchar(50)," +
                "$FIELD_COVER varchar(400)," +
                "$FIELD_CID varchar(50))"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}