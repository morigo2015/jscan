package com.igor.my_scan.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ThermoDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ThermoDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "thermo.db";
    private static final int DATABASE_VERSION = 1;


    public ThermoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Log.e(LOG_TAG, "database file codes.db is absent!!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
