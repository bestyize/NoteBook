package com.yize.notebook.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NoteDatabase extends SQLiteOpenHelper {
    public static final String TABLE_NAME="notes";
    public static final String CONTENT="content";
    public static final String ID="id";
    public static final String TIME="time";
    public static final String MODE="mode";
    public NoteDatabase(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"(" +
                ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                CONTENT+" TEXT NOT NULL," +
                TIME+" TEXT NOT NULL," +
                MODE+" INT DEFAULT 1" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
