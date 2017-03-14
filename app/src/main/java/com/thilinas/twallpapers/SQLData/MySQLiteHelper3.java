package com.thilinas.twallpapers.SQLData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Thilina on 22-Feb-17.
 */

public class MySQLiteHelper3 extends SQLiteOpenHelper {

    public static final String FAVPICS_TABLE_NAME = "favourites";
    public static final String FAVPICS_COLUMN_ID = "id";
    public static final String FAVPICS_COLUMN_PID = "pid";
    public static final String FAVPICS_COLUMN_NAME = "name";
    public static final String FAVPICS_COLUMN_URL = "url";
    public static final String FAVPICS_COLUMN_LIKES = "likes";
    public static final String FAVPICS_COLUMN_DLIKES = "dlikes";
    public static final String FAVPICS_COLUMN_ADATE = "adate";
    public static final String FAVPICS_COLUMN_ISFAV = "isfav";

    private static final String DATABASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " +FAVPICS_TABLE_NAME + "("+FAVPICS_COLUMN_ID+" INTEGER AUTO_INCREMENT PRIMARY KEY ,"+FAVPICS_COLUMN_PID+" TEXT,"+FAVPICS_COLUMN_NAME+" TEXT,"+FAVPICS_COLUMN_URL+" TEXT,"+FAVPICS_COLUMN_ISFAV+ " BOOLEAN,"+FAVPICS_COLUMN_LIKES+" INTEGER,"+FAVPICS_COLUMN_DLIKES+" INTEGER,"+FAVPICS_COLUMN_ADATE+" TEXT)" ;

    public MySQLiteHelper3(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper3.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + FAVPICS_TABLE_NAME);
        onCreate(db);
    }

}