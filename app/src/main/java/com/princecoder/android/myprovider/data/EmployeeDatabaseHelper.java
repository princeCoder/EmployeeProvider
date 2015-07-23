package com.princecoder.android.myprovider.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.princecoder.android.myprovider.data.EmployeeContract.EmployeeEntry;


 /*
 Help us create the database
    - here we define the database
    - We define the db version
    */

public class EmployeeDatabaseHelper extends SQLiteOpenHelper {

    // Database version
    public static final int DATABASE_VERSION = 2;

    //Database name
    public static final String DATABASE_NAME = "company.db";
    //String to create Employee Table
    public static final String CREATE_EMPLOYEE_TABLE =
            " CREATE TABLE " + EmployeeEntry.TABLE_NAME +
                    " (" + EmployeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EmployeeEntry.COLUMN_NAME + " TEXT NOT NULL, " + EmployeeEntry.COLUMN_TITLE + " TEXT NOT NULL);";

    public EmployeeDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the query to create the table
        db.execSQL(CREATE_EMPLOYEE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeEntry.TABLE_NAME);
        onCreate(db);
    }
}
