package com.tamadev.ssay_mp.utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class tamatools {


    public boolean checkDataBase(String Database_path)
    {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(Database_path, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {

        }
        return checkDB != null;
    }



}
