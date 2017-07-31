package com.mp3.downloader.music.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mp3.downloader.music.db.contract.AudioContract;
import com.mp3.downloader.music.db.contract.AudioLinkContract;

/**
 * Created by AlexandrVolkov on 27.06.2017.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "YoutubeAudio.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AudioLinkContract.SQL_CREATE_ENTRIES);
        db.execSQL(AudioContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(AudioLinkContract.SQL_DELETE_ENTRIES);
        db.execSQL(AudioContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
