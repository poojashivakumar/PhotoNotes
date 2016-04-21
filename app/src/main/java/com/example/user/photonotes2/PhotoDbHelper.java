package com.example.user.photonotes2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class PhotoDbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "PhotoNew2.db";

    public static final String KEY_ID = "id";
    public static final String DATABASE_TABLE = "photos";
    public static final String CAPTION = "caption";
    public static final String COLUMN = "imagePath";

     public PhotoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sqlQueryToCreateTable = "create table if not exists " + DATABASE_TABLE + "(" + KEY_ID + " integer primary key autoincrement," + CAPTION + " text," + COLUMN + " text" + ");";
        db.execSQL(sqlQueryToCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion == 1 && newVersion == 2){
            db.execSQL("ALTER TABLE DATABASE_TABLE ADD column FILE_PATH_COLUMN");
        }
    }
}




