package com.princecoder.android.myprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by prinzlyngotoum on 12/1/14.
 */
public class EmployeeDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Company";
    public static final String EMPLOYEE_TABLE_NAME = "employee";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_DB_TABLE =
            " CREATE TABLE " + EMPLOYEE_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL, " +
                    " position TEXT NOT NULL);";


    public EmployeeDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  EMPLOYEE_TABLE_NAME);
        onCreate(db);
    }
}
